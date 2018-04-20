package op;

import cc.mallet.fst.CRF;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.LineGroupIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;
import cc.mallet.util.FileUtils;
import org.omg.CORBA.INTERNAL;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MalletController {

    CRF model;

    public MalletController(String path) throws IOException {

        File f = new File("model");
        if (f == null) {
            throw new FileNotFoundException("File not found");
        }
        model = (CRF) FileUtils.readObject(f);
    }

    public ArrayList  getOutput(String inputString) throws IOException {

        Pipe pipe = model.getInputPipe();

        ArrayList labels = new ArrayList();


        InstanceList predictor = new InstanceList(pipe);

        Iterator<Instance> predictIt = new LineGroupIterator(

                new StringReader(inputString),
                Pattern.compile("^\\s*$"), true

        );

        predictor.addThruPipe(predictIt);

        Sequence[] m = model.predict(predictor);
        String w[] = m[0].toString().split(" ");
        ArrayList<Integer> alreadyVisited = new ArrayList<Integer>();
        int plot = 0;
        int story = 0;
        int mood = 0;
        int chars = 0;
        int other = 0;
        int bgs = 0;
        for (int i = 0; i < w.length; i++) {
            if (w[i].toString().equals("PLOT")){
                plot++;
            }
            else if (w[i].toString().equals("MOOD")){
                mood++;
            }
            else if (w[i].toString().equals("CHAR")){
                chars++;
            }
            else if (w[i].toString().equals("BGS")){
                bgs++;
            }

            else if (w[i].toString().equals("STORY")){
                story++;
            }
            else if (w[i].toString().equals("OTHER")){
                other++;
            }
        }

        alreadyVisited.add(plot);
        alreadyVisited.add(story);
        alreadyVisited.add(mood);
        alreadyVisited.add(chars);
        alreadyVisited.add(other);
        alreadyVisited.add(bgs);



        return alreadyVisited;

    }


}
