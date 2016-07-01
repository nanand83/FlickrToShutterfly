package ravensproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
	
	private String problemType;
	
    public Agent() {
        
    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return an int representing its
     * answer to the question: 1, 2, 3, 4, 5, or 6. Strings of these ints 
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName(). Return a negative number to skip a problem.
     * 
     * Make sure to return your answer *as an integer* at the end of Solve().
     * Returning your answer as a string may cause your program to crash.
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {
    	RavensFigure figA = problem.getFigures().get("A");
    	RavensFigure figB = problem.getFigures().get("B");
    	RavensFigure figC = problem.getFigures().get("C");
    	
    	
    	
    	
    	Map<String, RavensFigure> answersMap = new HashMap<String, RavensFigure>();
    	int options = 0;
    	String problemType = problem.getProblemType();
    	if (problemType.equals("2x2")) {
    		options = 6;
    	} else if (problemType.equals("3x3")) {
    		options = 8;
    	}
    	for (int i=1; i<=options; i++) {		//Answers 1 to 6
			String ans = String.valueOf(i);
			answersMap.put(ans, problem.getFigures().get(ans));
		}

    	Set<String> potentialAnswers = new HashSet<String>(); 
    	
    	
    	
    	//2x2 solving..
    	int[][] figAMatrix = getBinaryArray(figA.getVisual());
    	int[][] figBMatrix = getBinaryArray(figB.getVisual());
    	int[][] figCMatrix = getBinaryArray(figC.getVisual());
    	
    	if (problemType.equals("3x3")) {
    		RavensFigure figD = problem.getFigures().get("D");
    		RavensFigure figE = problem.getFigures().get("E");
    		RavensFigure figF = problem.getFigures().get("F");
    		RavensFigure figG = problem.getFigures().get("G");
    		RavensFigure figH = problem.getFigures().get("H");
    		
    		
    		int[][] figDMatrix = getBinaryArray(figD.getVisual());
    		int[][] figEMatrix = getBinaryArray(figE.getVisual());
    		int[][] figFMatrix = getBinaryArray(figF.getVisual());
    		int[][] figGMatrix = getBinaryArray(figG.getVisual());
    		int[][] figHMatrix = getBinaryArray(figH.getVisual());
    		
    		Map<String,String> transformationMap = new HashMap<String,String>();
    		transformationMap.put("AB", generateTransformation(figAMatrix, figBMatrix));
    		transformationMap.put("BC", generateTransformation(figBMatrix, figCMatrix));
    		transformationMap.put("DE", generateTransformation(figDMatrix, figEMatrix));
    		transformationMap.put("EF", generateTransformation(figEMatrix, figFMatrix));
    		transformationMap.put("GH", generateTransformation(figGMatrix, figHMatrix));
    		
    		transformationMap.put("AE", generateTransformation(figAMatrix, figEMatrix));
    		
    		transformationMap.put("AD", generateTransformation(figAMatrix, figDMatrix));
    		transformationMap.put("BE", generateTransformation(figBMatrix, figEMatrix));
    		transformationMap.put("CF", generateTransformation(figCMatrix, figFMatrix));
    		transformationMap.put("DG", generateTransformation(figDMatrix, figGMatrix));
    		transformationMap.put("EH", generateTransformation(figEMatrix, figHMatrix));
    		
    		System.out.println(transformationMap);
    		
    		potentialAnswers.addAll(applyAndTestTransformations(problemType, transformationMap, figFMatrix, figHMatrix, figEMatrix, answersMap));
    		System.out.println("Potentials: " +potentialAnswers);
    		
    		if (potentialAnswers.size() > 1) {
    			
    		}
    		
    	}
    	
    	else {
	    	int[][] ABtransformMatrix = matrixSubtract(figAMatrix, figBMatrix);
	    	
	    			
	    	if (potentialAnswers.size() == 0) {
	    		//Store transforms, generate and test..
	    		
	    		Map<String,String> transformationMap = new HashMap<String,String>();
	    		transformationMap.put("AB", generateTransformation(figAMatrix, figBMatrix));
	    		transformationMap.put("BC", generateTransformation(figBMatrix, figCMatrix));
	    		transformationMap.put("AC", generateTransformation(figAMatrix, figCMatrix));
	    		System.out.println(transformationMap);
	    		potentialAnswers.addAll(applyAndTestTransformations(problemType, transformationMap, figAMatrix, figBMatrix, figCMatrix, answersMap));
	    		System.out.println(potentialAnswers);
	    	}
	    	/*if (potentialAnswers.size()>1) {	//Try another set of generation..
	    		potentialAnswers.clear();
	    		Map<String,String> transformationMap = new HashMap<String,String>();
	    		transformationMap.put("AB", generateTransformation(figBMatrix, figAMatrix));
	    		transformationMap.put("BC", generateTransformation(figBMatrix, figCMatrix));
	    		transformationMap.put("AC", generateTransformation(figCMatrix, figAMatrix));
	    		System.out.println("Trans-2"+transformationMap);
	    		potentialAnswers.addAll(applyAndTestTransformations(problemType, transformationMap, figAMatrix, figBMatrix, figCMatrix, answersMap));
	    	}*/
	    	
	    	if (potentialAnswers.size() == 0 || potentialAnswers.size() > 1) {
		    	potentialAnswers.addAll(testSubtract(ABtransformMatrix, figCMatrix, answersMap));
	    	}
    	}
    	
    	int answer = -1;
    	if (potentialAnswers.size() == 1) {
    		answer = Integer.parseInt((String)potentialAnswers.toArray()[0]);
    	} 
    	
    	System.out.println("Answer: "+answer);
    	return answer;
    }
    
    
    private void printMatrix(int[][] testMatrix) {
		for (int i=0;i<testMatrix.length;i++) {
			for (int j=0; j<testMatrix[i].length;j++) {
				System.out.print(testMatrix[i][j]);
			}
			System.out.println();
		}
	}
	private String generateTransformation(int[][] a, int[][] b) {
    	int[][] testMatrix ;
    	Double matchPctg;
    	/* Identity */
    	if (isIdentical(a,b)) {
    		matchPctg = getMatchPctg(a, b);
    		if (matchPctg == 100.0) {
    			return "identity";
    		} else {
    			return "almost_identity";
    		}
    	}
    	
    	if (isFilled(a,b)) 
    		return "fill";
    	
    	
    	
    	/*
    	double countOfOnesAdash = 0;
    	double countOfOnesTest = 0;
    	double countOfZerosB = 0;
    	double countOfOnesA = 0;
    	double countOfOnesB = 0;
    	
    	testMatrix = matrixSubtract(a, b);
    	int[][] aComplement = complement(a);
    	
    	for (int i=0;i<a.length;i++) {
    		for (int j=0;j<a[i].length;j++) {
    			if (aComplement[i][j] == 1) 
    				countOfOnesAdash++;
    			if (b[i][j] == 0) 
    				countOfZerosB++;
    			if (testMatrix[i][j] == 1) 
    				countOfOnesTest++;
    			if (a[i][j] == 1)
    				countOfOnesA++;
    			if (b[i][j] == 1)
    				countOfOnesB++;
    		}
    	}
    	System.out.println("Countofonesadash - Count0B + count1A:"+(countOfOnesAdash-countOfZerosB+countOfOnesA));
    	System.out.println("Count1B: "+countOfOnesB);
    	if(((countOfOnesAdash-countOfZerosB+countOfOnesA)/countOfOnesB)*100 > 95.0) {
    		return "filled";
    	}
    	
    	//Find % of '1' pixels in testMatrix to % of '0' pixels in a..
    	// if % is ~50 then half -filled
    	// if % is ~100 then fully filled
    	Double countA = 0.0; Double countB = 0.0;
    	for (int m=0;m<a.length;m++) {
    		for (int n=0; n<a[m].length;n++) {
    			if (subMatrix[m][n] == 1) 
    				countA++;
    			if (testMatrix[m][n] == 1) 
    				countB++;
    		}
    	} 
    	Double pctg = ((countB - countA)/countB)*100.0;
    	System.out.println("pctg: "+pctg);
    	if (pctg > 45.0 && pctg <55.0) {
    		return "half-filled";
    	}
    	if (pctg > 90.0) {
    		return "filled";
    	}*/
    	
    	/* First check reflection */
    	testMatrix = flipLeft(a);
    	testMatrix = flipLeft(testMatrix);
    	if (isIdentical(b, testMatrix)) {
    		//System.out.println("a and b matches for reflection");
    		return "reflection";
    	}
    	
    	/*Rotation*/
    	testMatrix = flipLeft(a);
    	if (isIdentical(b, testMatrix)) {
    		//System.out.println("a and b matches for clockwise rotation");
    		
    		int [][] test2Matrix = flipRight(a);
    		test2Matrix = flipRight(test2Matrix);
    		test2Matrix = flipRight(test2Matrix);
    		if (isIdentical(b, test2Matrix)) {
    			return "reflection_2";
    		}
    		else {
    			return "clockwise_rotation";
    		}
    		
    	} else {    	
    		testMatrix = flipLeft(testMatrix);
    		testMatrix = flipLeft(testMatrix);
        	if (isIdentical(b, testMatrix)) {
        		//System.out.println("a and b matches for anticlockwise rotation");
        		int[][] test3Matrix = flipRight(a);
        		if (isIdentical(b, test3Matrix)) {
        			return "reflection_2";
        		}
        		else {
        			return "anticlockwise_rotation";
        		}
        	} else {
	    		//System.out.println("No transforms found");
	    		/*if (isIdentical(a,b)) {
	    			return "almost_identical";
	    		} else if (isHalfFilled(a,b)) {
	        		return "half-fill";
	    		} else*/
        		
	    			return String.valueOf(getMatchPctg(a,b));
        	}
    	}
 	
    }
    
	private boolean isHalfFilled(int[][] a, int[][] b) {
		int[][] tmpMatrix = new int[a.length][a[0].length];
		for (int i=0; i<a.length;i++) {
			for (int j=0;j<a[i].length;j++) {
				tmpMatrix[i][j] = b[i][j];
				if (a[i][j] == 0 && b[i][j] == 1) {
					tmpMatrix[i][j] = 0;
				} 
			}
		}
		if (isIdentical(a, tmpMatrix)) {
			return true;
		}
		return false;
	}
	private boolean isFilled(int[][] a, int[][] b) {
		
		//Checking for fill in b..
    	int[][] fillMatrixA = copy(a);
    	int[][] fillMatrixB = copy(b);
    	
    	if ( (addFill(a, fillMatrixA) && isIdentical(fillMatrixA, b)) || 
    			(addFill(b, fillMatrixB) && isIdentical(fillMatrixB, a)) ){
    		return true;
    	} 
    	return false;
	}
	
	private boolean addFill(int[][] src, int[][] target) {
		boolean addedFill = false;
		for (int i=0;i<src.length;i++) {
    		int startIndex = -1;
    		int endIndex = -1;
    		for (int m=0;m<src[i].length;m++) {
    			if (src[i][m] == 1) {
    				startIndex = m;
    				break;
    			}
    		}
    		for (int m=src[i].length-1;m>=0;m--) {
    			if (src[i][m] == 1) {
    				endIndex = m;
    				break;
    			}
    		}
    		
    		for (int n=startIndex; n<=endIndex; n++) {
    			if (startIndex != -1 && endIndex !=-1) {
    				if (target[i][n] == 0) {
    					target[i][n] = 1;
    					addedFill = true;
    				}
    			}
    		}
    		
    	}
		//System.out.println("Addedfill? "+addedFill);
		return addedFill;
	}
	
	
    private int[][] copy(int[][] a) {
    	int[][] returnMatrix = new int[a.length][a[0].length];
		for (int m=0;m<a.length;m++) {
			for (int n=0; n<a[m].length;n++) {
				returnMatrix[m][n] = a[m][n];				
			}
		}
		return returnMatrix;
	}
	private int[][] getAllOneMatrix(int[][] a) {
    	int[][] returnMatrix = new int[a.length][a[0].length];
		for (int m=0;m<a.length;m++) {
			for (int n=0; n<a[m].length;n++) {
				returnMatrix[m][n] = 1;				
			}
		}
		return returnMatrix;
	}
	private int[][] complement(int[][] a) {
		int[][] returnMatrix = new int[a.length][a[0].length];
		for (int m=0;m<a.length;m++) {
			for (int n=0; n<a[m].length;n++) {
				if (a[m][n] == 0) 
					returnMatrix[m][n] = 1;
				else
					returnMatrix[m][n] = 0;
			}
		}
		return returnMatrix;
	}
	private int[][] xorMatrix(int[][] a, int[][] b) {
    	int[][] newMatrix = new int[a.length][a[0].length];
    	
		for (int i=0;i<a.length;i++) {
			for (int j=0;j<a[i].length; j++) {
				newMatrix[i][j] = a[i][j] ^ b[i][j];
			}
		}
		return newMatrix;
	}
	
	private int[][] addMatrix(int[][] a, int[][] b) {
		int[][] newMatrix = new int[a.length][a[0].length];
    	
		for (int i=0;i<a.length;i++) {
			for (int j=0;j<a[i].length; j++) {
				int val = 0;
				val = a[i][j] + b[i][j];
				if (val > 1) 
					newMatrix[i][j] = 1;
				else
					newMatrix[i][j] = val;
			}
		}
		return newMatrix; 
	}
	
    private Double getMatchPctg(int[][] a, int[][] b) {
    	double totalCells = a.length * a[0].length;
    	double matches = 0;
    	
    	for (int i=0; i<a.length; i++) {
    		for (int j=0; j< a[i].length; j++) {
    			if (a[i][j] == b[i][j]) { 
    				matches++;
    			}
    		}
    	}
    	//System.out.println("Totalcells: "+totalCells+" And Matches:"+matches);
    	double matchPctg = (matches/totalCells) * 100;
    	//System.out.println("Match %:"+matchPctg);
    	return matchPctg;
    }
    
    private Set<String> applyAndTestTransformations(String type, Map<String,String> transformationMap, int[][] figAMatrix,int[][] figBMatrix, int[][] figCMatrix, Map<String, RavensFigure> answersMap) {
    	Map<String, Map<String,Double>> testResultsMap = new HashMap<String, Map<String,Double>>();
    	for (String answer : answersMap.keySet()) {
    		int[][] ansMatrix = getBinaryArray(answersMap.get(answer).getVisual());
    		
    		System.out.println("Now checking for :"+answer);
    		String aXTransform = generateTransformation(figAMatrix, ansMatrix);
    		String bXTransform = generateTransformation(figBMatrix, ansMatrix);
    		String cXTransform = generateTransformation(figCMatrix, ansMatrix);
    		System.out.println("{aX:"+aXTransform+", bX:"+bXTransform+", cX: "+cXTransform+"}");
    		
    		
    		Map<String,Double> transformResultsMap = new HashMap<String, Double>(); 
    		
    		List ABCTransformList = new ArrayList<String>();
    		ABCTransformList.add(transformationMap.get("AB"));
    		ABCTransformList.add(transformationMap.get("BC"));
    		
    		List DEFTransformList = new ArrayList<String>();
    		DEFTransformList.add(transformationMap.get("DE"));
    		DEFTransformList.add(transformationMap.get("EF"));
    		
    		List GHITransformList = new ArrayList<String>();
    		GHITransformList.add(transformationMap.get("GH"));
    		GHITransformList.add(bXTransform);
    		
    		List ADGTransformList = new ArrayList<String>();
    		ADGTransformList.add(transformationMap.get("AD"));
    		ADGTransformList.add(transformationMap.get("DG"));
    		
    		List BEHTransformList = new ArrayList<String>();
    		BEHTransformList.add(transformationMap.get("BE"));
    		BEHTransformList.add(transformationMap.get("EH"));
    		
    		List CFITransformList = new ArrayList<String>();
    		CFITransformList.add(transformationMap.get("CF"));
    		CFITransformList.add(aXTransform);
    		
    		List AEITransformList = new ArrayList<String>();
    		AEITransformList.add(transformationMap.get("AE"));
    		AEITransformList.add(cXTransform);
    		
    		
    		if (type.equals("3x3")) {
    			transformResultsMap.put("aXTransform", testTransformation(ADGTransformList, BEHTransformList, CFITransformList));
        		transformResultsMap.put("bXTransform", testTransformation(ABCTransformList, DEFTransformList, GHITransformList));
    		} else {
    			transformResultsMap.put("aXTransform", testTransformation(aXTransform, transformationMap.get("BC")));
    			transformResultsMap.put("bXTransform", testTransformation(bXTransform, transformationMap.get("AC")));
    			transformResultsMap.put("cXTransform", testTransformation(cXTransform, transformationMap.get("AB")));
    		}
    		
    		testResultsMap.put(answer, transformResultsMap);
    	}
    	System.out.println("testresultsmap: "+testResultsMap);
    	return getPotentialAnswers(type, testResultsMap);
	}
    
    
    
	private Double testTransformation(List<String> list1,
			List<String> list2, List<String> list3) {
		
    	Double confidenceLevel = 0.0;
    	
    	if (list1.get(0).equals(list2.get(0)) && 
    			list2.get(0).equals(list3.get(0)) && 
    			list1.get(1).equals(list2.get(1)) &&
    			list2.get(1).equals(list3.get(1))) {
    		confidenceLevel = 100.0;
    	} else if (areAllDouble(list1,list2,list3)) {
    		Double matchPctgDelta1 = Double.parseDouble(list1.get(0)) - Double.parseDouble(list1.get(1));
    		Double matchPctgDelta2 = Double.parseDouble(list2.get(0)) - Double.parseDouble(list2.get(1));
    		Double matchPctgDelta3 = Double.parseDouble(list3.get(0)) - Double.parseDouble(list3.get(1));
    		
    		Double std_deviation = getStandardDeviation(matchPctgDelta1, matchPctgDelta2, matchPctgDelta3);
    		confidenceLevel = 100.0 - std_deviation;
    	}
    	
    	return confidenceLevel;
	}
	private Double getStandardDeviation(Double matchPctgDelta1,
			Double matchPctgDelta2, Double matchPctgDelta3) {
	
		Double mean =  (matchPctgDelta1+matchPctgDelta2+matchPctgDelta3)/3.0;
		
		Double variance = ( Math.pow((matchPctgDelta1-mean), 2) +
							Math.pow((matchPctgDelta2-mean), 2) + 
							Math.pow((matchPctgDelta3-mean), 2) ) / 3.0;
		
		return Math.sqrt(variance);
		
	}
	private boolean areAllDouble(List<String> list1, List<String> list2,
			List<String> list3) {
		if (isDouble(list1.get(0)) && isDouble(list1.get(1)) &&
				isDouble(list2.get(0)) && isDouble(list2.get(1)) &&
				isDouble(list3.get(0)) && isDouble(list3.get(1))) {
			return true;
		} 
		return false;
	}
	

	private Set<String> getPotentialAnswers(String type, Map<String,Map<String, Double>> testResultsMap) { 
    	Set<String> potentialAnswers = new HashSet<String>();
    	List<String> transforms = new ArrayList<String>();
    	transforms.add("aXTransform");
		transforms.add("bXTransform");
    	if (type.equals("2x2")) {
    		transforms.add("cXTransform");
    	}
    	Map<String,String> potentialAnswerMap = new HashMap<String,String>();
    	/* For each transform, create a potential answermap */
    	for (String eachTransform : transforms) {
    		Double confLevel = 0.0;
    		//Set<String> answer = new HashSet<String>();
    		String answer = null;
    		for (String key: testResultsMap.keySet()) {
    			Map<String,Double> map = testResultsMap.get(key);
    			if (map.get(eachTransform) > 95.0 && map.get(eachTransform) >= confLevel) {
    				//answer.add(key);
    				answer = key;
    				confLevel = map.get(eachTransform);
    			}
    		}
    		if (answer != null) {
    			potentialAnswerMap.put(eachTransform,answer);
    		}
    	}
    	//System.out.println("Potentialanswermap: "+potentialAnswerMap);
    	/*Now iterate thru potentialanswermap and vote for # of occurrences*/
    	Map<String, Integer> answerVotesMap = new HashMap<String,Integer>();
    	int count;
    	for (String key : potentialAnswerMap.keySet()) { 
    			String eachAns = potentialAnswerMap.get(key);
    			if (answerVotesMap.containsKey(eachAns)) {
    				count = answerVotesMap.get(eachAns)+1;
    			} else {
    				count = 1;
    			}
    			answerVotesMap.put(eachAns, count);
    	}
    	System.out.println("ansvotesmap: "+answerVotesMap);
    	
    	/* Now arrive at final answer(s)*/
    	int votes = 0;
    	for (String key : answerVotesMap.keySet()) {
    		int answerVotes = answerVotesMap.get(key);
    		if (answerVotes > votes) {
    			potentialAnswers.clear();
    			votes = answerVotes;
    			potentialAnswers.add(key);
    		} else if (answerVotes == votes) {
    			potentialAnswers.add(key);
    		}
    	}
    	
    	return potentialAnswers;
    }
    
    private boolean isDouble(String s) {
    	try {
    		Double.parseDouble(s);
    	} catch (NumberFormatException nfe) {
    		return false;
    	}
    	return true;
     } 
    
    public Double testTransformation(String a, String b) {
    	Double confidenceLevel = 0.0;
    	
    	if (isDouble(a) && isDouble(b)) {
    		confidenceLevel = (1 - Math.abs(Double.parseDouble(a) - Double.parseDouble(b))) * 100;
    	} else {		
    		if (a.equalsIgnoreCase(b)) {  //Direct match..
    			confidenceLevel = 100.0;
    		}
    	}
    	return confidenceLevel;
    }
    
    
	public boolean isReflection(int[][] a, int[][] b, int[][] c) {
    	//Check if reflection along  right for a and b..
    	int[][] testMatrix ;
    	testMatrix = flipLeft(a);
    	if (isIdentical(b, testMatrix)) {
    		//System.out.println("a and b matches");
    		testMatrix = flipLeft(b);
    		if (isIdentical(c, testMatrix)) {
    			//System.out.println("c and B matches");
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    
    public int[][] flipRight(int[][] matrix)
    {
    	int w = matrix.length;
        int h = matrix[0].length;
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[w - j - 1][i];
            }
        }
        return ret;
    }

    private int[][] rotate(int[][] arr) {
        int width = arr[0].length;
        int depth = arr.length;
        int[][] re = new int[width][depth];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < width; j++) {
                re[j][depth - i - 1] = arr[i][j];
            }
        }
        return re;
    }
    
    public int[][] flipLeft(int[][] matrix)
    {
        int w = matrix.length;
        int h = matrix[0].length;   
        int[][] ret = new int[h][w];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                ret[i][j] = matrix[j][h - i - 1];
            }
        }
        return ret;
    }
    
    public Set<String> testSubtract(int[][] transformMatrix, 
    						int[][] targetMatrix, Map<String, RavensFigure> answersMap)  {
    	Set<String> potentialAnswers = new HashSet<String>();
    	for (String answer : answersMap.keySet()) {
    		int[][] ansMatrix = getBinaryArray(answersMap.get(answer).getVisual());
    		
    		System.out.println("Now applying transform for :"+answer);
    		
			int[][] ansTransformMatrix ;
    		
			ansTransformMatrix = matrixSubtract(targetMatrix, ansMatrix);
    		if (isIdentical(transformMatrix, ansTransformMatrix)) {
    			//System.out.println(answer+" Matches.. Potential answer");
    			potentialAnswers.add(answer);
    		}
    	}
    	
    	return potentialAnswers;
    }
    
    public int[][] getBinaryArray(String fileName) {
    	int[][] binArray = null;
    	File f = new File(fileName);
    	BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
			binArray = new int[image.getWidth()][];
	
	    	for (int x = 0; x < image.getWidth(); x++) {
	    		binArray[x] = new int[image.getHeight()];
	
	    	    for (int y = 0; y < image.getHeight(); y++) {
	    	    	binArray[x][y] = (image.getRGB(x, y) == 0xFFFFFFFF ? 0 : 1);
	    	    }
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Remove fill and keep only the outline..
		/*int[][] unfilledBinArray = new int[binArray.length][binArray[0].length];
		for (int a=0; a<binArray.length; a++) {
			for (int b=0; b<binArray[a].length;b++) {
				if (a > 0 && b > 0 && a<binArray.length-1 && b<binArray[a].length-1) {
					if (binArray[a][b-1] != 0 &&
						binArray[a][b+1] !=0 &&
						binArray[a-1][b] !=0 && 
						binArray[a+1][b] !=0 &&
						binArray[a][b] == 1) { 		//Not an edge but a fill.. Safe to remove.. 
						//binArray[a][b] = 0;
						unfilledBinArray[a][b] = 0;
					} else {
						unfilledBinArray[a][b] = binArray[a][b];
					}
				} else { 
					unfilledBinArray[a][b] = binArray[a][b];
				}
			}
		}
		
		
		if (fileName.equals("Problems/Basic Problems B/Basic Problem B-07/A.png")) {
			for (int m=0; m< unfilledBinArray.length;m++) {
				for(int n=0; n<unfilledBinArray[m].length; n++) {
					System.out.print(unfilledBinArray[m][n]);
				}
				System.out.println();
			}
		}*/
    	return binArray;

    }
    
    
    public boolean isIdentical(int[][] a, int[][] b) {
    	if (getMatchPctg(a,b) > 95.0) {
    		return true;
    	}
    	return false;
    }
    
    public int[][] matrixSubtract(int[][] a, int[][] b) {
    	int[][] subtractedMatrix = new int[b.length][b[0].length];
    	
    	for (int i=0; i< b.length; i++) {
    		for (int j=0; j< b[i].length; j++) {
    			int val =  b[i][j] - a[i][j];
    			subtractedMatrix[i][j] = val;
    		}
    	}
    	
    	return subtractedMatrix;
    }
    
}
