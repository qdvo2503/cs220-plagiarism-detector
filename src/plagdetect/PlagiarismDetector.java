package plagdetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	
	Set<String> resultsus = new HashSet<>();
	Map<String, Set<String>> ngramMap = new HashMap<>();
	Map<String, Map<String, Integer>> allFile = new HashMap<>();
	int N;
	
	public PlagiarismDetector(int n) {
		// TODO implement this method
		N = n;
	}
	
	@Override
	public int getN() {
		// TODO Auto-generated method stub
		return N;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		return allFile.keySet();
		
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return ngramMap.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return getNgramsInFile(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		return allFile;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method
		Scanner sc = new Scanner(file);
		String filename1 = file.getName();
		Set<String> ngram = new HashSet<>();
		Map<String, Integer> map = new HashMap<>();
		Map<String, Integer> mapt = new HashMap<>();
		while (sc.hasNextLine()) {
			String s = sc.nextLine();
			String[] word = s.split(" ");
			int i = 0;
			while (i <= word.length - N) {
				ngram.add(String.join(" ",  Arrays.copyOfRange(word, i, i+N)));
				i++;
			}
		}

		for (Map.Entry<String, Set<String>> getfile : ngramMap.entrySet()) {
			String tname = getfile.getKey();
			Set<String> tSet = new HashSet<>(ngram);
			Set<String> tset = (HashSet<String>) getfile.getValue();
			tSet.removeAll(tset);
			int num = ngram.size() - tSet.size();
			mapt.put(tname, num);
			allFile.get(tname).put(filename1, num);
		}

		ngramMap.put(filename1, ngram);
		allFile.put(filename1, mapt);
		
	}

	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		return allFile.get(file1).get(file2);
	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
		// TODO Auto-generated method stub
		for (Map.Entry p1 : allFile.entrySet()) {
			String file1 = p1.getKey().toString();
			for (Map.Entry p2 : allFile.get(file1).entrySet()) {
				int v = (int) p2.getValue();
				String file2 = p2.getKey().toString();
				if (v >= minNgrams) {
					if (file1.compareTo(file2) < 0) {
						String str1 = file1 + " " + file2 + " " + String.valueOf(v) ;
						resultsus.add(str1);
						} String str2 = file2 + " " + file1 + " " + String.valueOf(v) ;
						resultsus.add(str2);
				}
			}
		}
		
		for (String str : resultsus) {
			System.out.println("Suspicious pairs:" + str);
		} return resultsus;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}

