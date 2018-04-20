package op;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cc.mallet.types.Sequence;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:8000")
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/greeting", method = RequestMethod.POST)
    public GreetingResponse greeting(@RequestBody Greeting payload) throws Exception {
        MalletController mc = new MalletController("/file/model");

        // Process input data......................................


        String inputReview = payload.getContent();
        String[] sentences;
        File modelIn = new File("en-sent.bin");

        // Split into sentences .....................................

        SentenceModel sent_model = new SentenceModel(modelIn);


        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sent_model);
        sentences = sentenceDetector.sentDetect(inputReview);

        // POS tagger model ..........................................

        POSModel model = new POSModelLoader().load(new File("en-pos-maxent.bin"));
        POSTaggerME tagger = new POSTaggerME(model);

        // Tokenezier model ..........................................

        modelIn = new File("en-token.bin");
        TokenizerModel token_model = new TokenizerModel(modelIn);
        TokenizerME tokenizer = new TokenizerME(token_model);
        String tokens[] = {"x"};
        String tags[] = {"x"};
        String finalString = "";

        HashMap<Sequence,Integer> freq;
        String sw = getStopWords();




        for (String sentence : sentences) {


            tokens = tokenizer.tokenize(sentence);

            List<String> finalTokens = new ArrayList<>();

            for(String token: tokens) {
                if(Pattern.matches(sw,token)) {
                    continue;
                }
                else{
                    finalTokens.add(token);
                }
            }

           String[] t =  finalTokens.toArray(new String[finalTokens.size()]);




            tags = tagger.tag(t);


            for (int i = 0; i < tags.length; i++) {
                String sent = 0 + (int)(Math.random() * ((1 - 0) + 1)) == 0 ? "POSTIVE" : "NEGATIVE";
                finalString += t[i].toLowerCase() + " " + tags[i] + " " + sent + "\n";
            }


        }

        ArrayList<Integer> counts = new ArrayList<Integer>();

        counts = mc.getOutput(finalString);

        int plot = counts.get(0);
        int mood = counts.get(2);
        int chars = counts.get(3);
        int bgs = counts.get(4);
        int other = counts.get(5);

        return new GreetingResponse(plot,mood, chars,bgs, other);

    }

    public String getStopWords() throws FileNotFoundException, IOException {

        // Open the file
        ArrayList words = new ArrayList();
        FileInputStream fstream = new FileInputStream("stopwords.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            words.add(strLine);

        }

        String finalString = String.join("|", words);
        br.close();
        finalString = "\\b(?:" +finalString+ ")"+ "\\b";
        return finalString;


    }


}