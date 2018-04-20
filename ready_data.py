import glob
import nltk
from nltk.corpus import stopwords
import re
from random import randint
	


def get_stop_words():
	stop_words = set(stopwords.words('english'))
	stop_words.update(['.', ',', '"', "'", '?', '!', ':', ';', '(', ')', '[', ']', '{', '}']) # remove it if you need punctuation 
	return stop_words

def write_to_output_file(file_list, t):
	for file in file_list:
		with open(file,'r') as f:
			file_text = f.read()
			sentences = nltk.sent_tokenize(file_text)

			for sentence in sentences:

				sentence  =  re.sub("[^a-zA-Z0-9\t\r\s]","",sentence)
				tokenize_text = nltk.word_tokenize(sentence)
				stopwords = get_stop_words()

				tokens = [word for word in tokenize_text if word not in stopwords]


				pos = nltk.pos_tag(tokens)

				for i in range(len(tokens)):
					with open("output.txt","a") as o:
						o.write(pos[i][0] + " " +  pos[i][1] + " " + t + " " + get_label(pos[i][0], pos[i][1]) +"\n" )


def get_label(text,pos):

	rem_label = ["CHAR", "OTHER", "PLOT"]

	label = ["OTHER", "PLOT", "BGS", "MOOD"]

	if re.search('plot|story',text):
		return "PLOT"

	if re.search('music',text):
		return "BGS"

	if pos == 'NNP':
		return "CHAR"

	if pos == 'JJ' or pos == 'JJR' or pos == 'JJS' or pos == 'RB' or pos == 'RBR' or pos== 'RBS':
		return "MOOD"


	if pos == 'NNS' or pos == 'NN' :	
		return rem_label[randint(0,2)]


	if re.search('VB.?',pos):
		return "PLOT"	

	return label[randint(0,3)]



if __name__=='__main__':


	neg_files = glob.glob("neg/*.txt")
	pos_files = glob.glob("pos/*.txt")

	write_to_output_file(pos_files, "POSITIVE")
	write_to_output_file(neg_files, "NEGATIVE")


