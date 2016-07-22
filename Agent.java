package ravensproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
    	
    	String problemName = problem.getName();
    	
    	
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
    		
    		int[][] expectedMatrix = null;
    		
    		/* Project 3------*/
    		//Rule 1
    		if ( (isIdentical(figAMatrix, figBMatrix) && isIdentical(figBMatrix, figCMatrix))
    							&&
    			(isIdentical(figDMatrix, figEMatrix) && isIdentical(figEMatrix, figFMatrix))
    							&&
    			(isIdentical(figGMatrix, figHMatrix))
    		   ) {
    			expectedMatrix = figGMatrix;
    			System.out.println("Rule 1 Activated");
    		} 
    		//Rule 2
    		else if ( (isIdentical(figBMatrix, figFMatrix) )
    							&&
    				  (isIdentical(figDMatrix, figHMatrix) )
    				  			&&
    				  (isIdentical(figAMatrix, figEMatrix))
    				) {
    			
    			expectedMatrix = figAMatrix;
    			System.out.println("Rule 2 Activated");
    		}
    		//Rule 3
    		else if ( isIdentical(matrixSubtract(figFMatrix, figBMatrix), matrixSubtract(figEMatrix, figAMatrix))
    										&&
    				  isIdentical(matrixSubtract(figEMatrix, figAMatrix), matrixSubtract(figDMatrix, figCMatrix))
    				  						&&
    				  isIdentical(matrixSubtract(figHMatrix, figDMatrix), matrixSubtract(figGMatrix, figFMatrix))
    				) {
    			
    			expectedMatrix = matrixSubtract( matrixSubtract(figGMatrix, figFMatrix), figEMatrix);
    			System.out.println("Rule 3 Activated");
    		}
    		
    		
    		
    		
    		if (potentialAnswers.size()!=1 ) {
    			//Rule 8.. 
    			if (isIdentical(figBMatrix, figDMatrix) &&
    					isIdentical(figFMatrix, figHMatrix) &&
    					isIdentical(figCMatrix, figEMatrix) &&
    					isIdentical(figCMatrix, figGMatrix) && 
    					isIdentical(figAMatrix, figEMatrix) ) {
    				System.out.println("Rule 8 Activated");
    				potentialAnswers.clear();
    				expectedMatrix = figAMatrix;
    			}
    		}
    		
    		if (expectedMatrix != null) {
    			
    			potentialAnswers.addAll(getPotentialAnswersForExpectedMatrix(expectedMatrix, answersMap));
    			
       		}
    		
    		
    		
    		
    		if (potentialAnswers.size() != 1) {
    			if (isIdentical(figBMatrix, figDMatrix) &&
    					isIdentical(figFMatrix, figHMatrix) &&
    					isIdentical(figCMatrix, figEMatrix) &&
    					isIdentical(figCMatrix, figGMatrix) && 
    					!isIdentical(figAMatrix, figEMatrix) ) {
    				System.out.println("Rule 9 activated");
    				potentialAnswers.clear();
    				String aeTransform = generateTransformation(figAMatrix, figEMatrix);
    				System.out.println("aeTransform:"+aeTransform);
    				for (String eachAns: answersMap.keySet()) {
    					int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
    					String eiTransform = generateTransformation(figEMatrix, ansMatrix);
    					if (eiTransform.equals(aeTransform)) {
    						potentialAnswers.add(eachAns);
    					}
    						
    				}
    				
    			}
    		}
    		
    		if (potentialAnswers.size() != 1) {
    			//Rule 4
    			int identicalCount = 0;
    			if (isIdentical(figAMatrix, figBMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figCMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figDMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figEMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figFMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figAMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figCMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figDMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figEMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figFMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figBMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figCMatrix, figDMatrix)) identicalCount++;
    			if (isIdentical(figCMatrix, figEMatrix)) identicalCount++;
    			if (isIdentical(figCMatrix, figFMatrix)) identicalCount++;
    			if (isIdentical(figCMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figCMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figDMatrix, figEMatrix)) identicalCount++;
    			if (isIdentical(figDMatrix, figFMatrix)) identicalCount++;
    			if (isIdentical(figDMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figDMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figEMatrix, figFMatrix)) identicalCount++;
    			if (isIdentical(figEMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figEMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figFMatrix, figGMatrix)) identicalCount++;
    			if (isIdentical(figFMatrix, figHMatrix)) identicalCount++;
    			if (isIdentical(figGMatrix, figHMatrix)) identicalCount++;
    			
    			if (identicalCount < 5) 
    			/*if ( (!isIdentical(figAMatrix, figBMatrix) && !isIdentical(figAMatrix, figCMatrix) && !isIdentical(figAMatrix, figDMatrix) && !isIdentical(figAMatrix, figEMatrix) && !isIdentical(figAMatrix, figFMatrix) && !isIdentical(figAMatrix, figGMatrix) && !isIdentical(figAMatrix, figHMatrix))
    							&&
    				 (!isIdentical(figBMatrix, figCMatrix) && !isIdentical(figBMatrix, figDMatrix) && !isIdentical(figBMatrix, figEMatrix) && !isIdentical(figBMatrix, figFMatrix) && !isIdentical(figBMatrix, figGMatrix) && !isIdentical(figBMatrix, figHMatrix))
    				 			&&
    				 (!isIdentical(figCMatrix, figDMatrix) && !isIdentical(figCMatrix, figEMatrix) && !isIdentical(figCMatrix, figFMatrix) && !isIdentical(figCMatrix, figGMatrix) && !isIdentical(figCMatrix, figHMatrix))
    				 			&&
    				 (!isIdentical(figDMatrix, figEMatrix) && !isIdentical(figDMatrix, figFMatrix) && !isIdentical(figDMatrix, figGMatrix) && !isIdentical(figDMatrix, figHMatrix))
    				 			&&
    		    	(!isIdentical(figEMatrix, figFMatrix) && !isIdentical(figEMatrix, figGMatrix) && !isIdentical(figEMatrix, figHMatrix))
    		    				&&
    		    	(!isIdentical(figFMatrix, figGMatrix) && !isIdentical(figFMatrix, figHMatrix))
    		    				&&
    		    	(!isIdentical(figGMatrix, figHMatrix))
    		    ) */{  	///All input images are different..
    				potentialAnswers.clear();
        			System.out.println("Rule 4 Activated");
    				//Check if answers matches with any of the images. If matches, eliminate that answer.
    				for (String eachAns: answersMap.keySet()) {
    					System.out.println("Checking "+eachAns);
        				int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
        				if (!isExactlyIdentical(ansMatrix, figAMatrix) && !isExactlyIdentical(ansMatrix, figBMatrix) && !isExactlyIdentical(ansMatrix, figCMatrix) && 
        					!isExactlyIdentical(ansMatrix, figDMatrix) && !isExactlyIdentical(ansMatrix, figEMatrix) && !isExactlyIdentical(ansMatrix, figFMatrix) && 
        					!isExactlyIdentical(ansMatrix, figGMatrix) && !isExactlyIdentical(ansMatrix, figHMatrix)) {
        					System.out.println("Adding "+eachAns);
        					potentialAnswers.add(eachAns);
        				}
    				}
    				if (potentialAnswers.size() > 1 && potentialAnswers.size()<5) {		//2 or 3 potentialanswers..
    					//Rule 4 B difference in darkpixel count..
    					if ( (getNumberOfDarkPixels(figAMatrix) - getNumberOfDarkPixels(figBMatrix)) == getNumberOfDarkPixels(figCMatrix)
    								&&
    						 (getNumberOfDarkPixels(figDMatrix) - getNumberOfDarkPixels(figEMatrix)) == getNumberOfDarkPixels(figFMatrix)
    						) {
    						System.out.println("Rule 4 B activated");
    						Set<String> tmpPotentialAnswers = new HashSet<String>();
    						tmpPotentialAnswers.addAll(potentialAnswers);
    						
    						for (String thisAns: potentialAnswers) {
    							int[][] thisAnsMatrix = getBinaryArray(answersMap.get(thisAns).getVisual());
    							if ( 100.0 - Math.abs( (getNumberOfDarkPixels(figGMatrix) - getNumberOfDarkPixels(figHMatrix)) - getNumberOfDarkPixels(thisAnsMatrix)) < 0.0) {
    								tmpPotentialAnswers.remove(thisAns);
    							}
    						}
    						potentialAnswers.clear();
    						potentialAnswers.addAll(tmpPotentialAnswers);
    					} else {
    						int[][] abXorMatrix = xorMatrix(figAMatrix, figBMatrix);
    						int[][] deXorMatrix = xorMatrix(figDMatrix, figEMatrix);
    						int[][] ghXorMatrix = xorMatrix(figGMatrix, figHMatrix);
    						int[][] bcXorMatrix = xorMatrix(figBMatrix, figCMatrix);
    						int[][] efXorMatrix = xorMatrix(figEMatrix, figFMatrix);
    						
    						if (isIdentical(abXorMatrix, deXorMatrix) && isIdentical(deXorMatrix, ghXorMatrix) 
    									&&
    							isIdentical(bcXorMatrix, efXorMatrix) ) {
    						
    							System.out.println("Rule 4 C activated");
    							
    							Set<String> tmpPotentialAnswers = new HashSet<String>();
        						tmpPotentialAnswers.addAll(potentialAnswers);
        						
        						for (String thisAns: potentialAnswers) {
        							int[][] thisAnsMatrix = getBinaryArray(answersMap.get(thisAns).getVisual());
        							int[][] hiXorMatrix = xorMatrix(figHMatrix, thisAnsMatrix);
        							double d = getMatchPctg(efXorMatrix, hiXorMatrix);
        							if (d < 95.0) {
        								tmpPotentialAnswers.remove(thisAns);
        							}
        						}
        						potentialAnswers.clear();
        						potentialAnswers.addAll(tmpPotentialAnswers);
    						}
    					}
    				}
    			}
    		}
    		
    		if (potentialAnswers.size() != 1) {
    			//Rule 5..addition..
    			if (isIdentical(addMatrix(figAMatrix, figDMatrix), figGMatrix)
    						&&
    				isIdentical(addMatrix(figBMatrix, figEMatrix), figHMatrix)
    				) {
    				potentialAnswers.clear();
    				System.out.println("Rule 5 Activated");
    				expectedMatrix = addMatrix(figCMatrix, figFMatrix);
    				if (expectedMatrix != null) {
    	    			potentialAnswers.addAll(getPotentialAnswersForExpectedMatrix(expectedMatrix, answersMap));
    	       		}
    			}
    		}
    		  
    		if (potentialAnswers.size() > 1) {
				//Rule 6 - XOr operation..
				if (isIdentical(xorMatrix(figAMatrix, figBMatrix), figCMatrix)
							&&
					isIdentical(xorMatrix(figDMatrix, figEMatrix), figFMatrix)
					) {
					System.out.println("Rule 6 Activated");
					potentialAnswers.clear();
					
					expectedMatrix = xorMatrix(figGMatrix, figHMatrix);
					
					if (expectedMatrix != null) {
		    			potentialAnswers.addAll(getPotentialAnswersForExpectedMatrix(expectedMatrix, answersMap));
		       		}
				}
			}
    		
    		if (potentialAnswers.size() > 1) {
				//Rule 7 - XNOr operation..
				if (isIdentical(specialXNorMatrix(figAMatrix, figBMatrix), figCMatrix)
							&&
					isIdentical(specialXNorMatrix(figDMatrix, figEMatrix), figFMatrix)
					) {
					System.out.println("Rule 7 Activated");
					potentialAnswers.clear();
					
					expectedMatrix = specialXNorMatrix(figGMatrix, figHMatrix);
					
					if (expectedMatrix != null) {
		    			potentialAnswers.addAll(getPotentialAnswersForExpectedMatrix(expectedMatrix, answersMap));
		       		}
				}
			}
    		
    		List<int[][]> imageList = new ArrayList<int[][]>();
    		imageList.add(figAMatrix);
    		imageList.add(figBMatrix);
    		imageList.add(figCMatrix);
    		imageList.add(figDMatrix);
    		imageList.add(figEMatrix);
    		imageList.add(figFMatrix);
    		imageList.add(figGMatrix);
    		imageList.add(figHMatrix);
    		if (potentialAnswers.size() != 1) {
    			//Rule 8: If there exists exactly 2 pairs of images 
    			// such that, specialxnor(image1, image2) = image1
    			// then, check from potential answers such that there forms
    			// another pair which satisfies specialxnor(image1,ans) = image1..
    			for (String thisAns : potentialAnswers) {    			
    				int[][] thisAnsMatrix = getBinaryArray(answersMap.get(thisAns).getVisual());
    				imageList.add(thisAnsMatrix);
    				for (int i=0; i< imageList.size(); i++) {
    					List<int[][]> listToCompare = new ArrayList<int[][]>();
    					listToCompare.addAll(imageList);
    					listToCompare.remove(i);			//Removing current image..
    					for (int j=0;j<listToCompare.size();j++) {
    						if (isExactlyIdentical(specialXNorMatrix(imageList.get(i), listToCompare.get(j)), imageList.get(i))) {
    							System.out.println("Image "+i+" and Image "+j+" are specialxnors");
    						}
    					}
    				}
    				imageList.remove(thisAnsMatrix);
    				
    				
    			}
    			
    		}
    		
    		
    		System.out.println("Potential answers:"+potentialAnswers);
    		/* ****         */
    		
    		
    		/* Project 2 -------------------------------
    		 * 
    		 *****************
    		Map<String,String> transformationMap = new HashMap<String,String>();
    		transformationMap.put("AB", generateTransformation(figAMatrix, figBMatrix));
    		transformationMap.put("BC", generateTransformation(figBMatrix, figCMatrix));
    		transformationMap.put("DE", generateTransformation(figDMatrix, figEMatrix));
    		transformationMap.put("EF", generateTransformation(figEMatrix, figFMatrix));
    		transformationMap.put("GH", generateTransformation(figGMatrix, figHMatrix));
    		
    		transformationMap.put("AE", generateTransformation(figAMatrix, figEMatrix));
    		transformationMap.put("CE", generateTransformation(figCMatrix, figEMatrix));
    		transformationMap.put("EG", generateTransformation(figEMatrix, figGMatrix));
    		
    		transformationMap.put("AD", generateTransformation(figAMatrix, figDMatrix));
    		transformationMap.put("BE", generateTransformation(figBMatrix, figEMatrix));
    		transformationMap.put("CF", generateTransformation(figCMatrix, figFMatrix));
    		transformationMap.put("DG", generateTransformation(figDMatrix, figGMatrix));
    		transformationMap.put("EH", generateTransformation(figEMatrix, figHMatrix));
    		
    		System.out.println(transformationMap);
    		System.out.println(isIdentical(figAMatrix, figEMatrix));
    		* Basic diagonal check *
    		if (isIdentical(figAMatrix, figEMatrix)) {	//Diagonal match.
    			for (String eachAns: answersMap.keySet()) {
    				int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
    				if (isIdentical(figEMatrix, ansMatrix)) {
    					potentialAnswers.add(eachAns);
    				}
				}
    		}
    		
    		
    		if (potentialAnswers.size() == 0) {
				if (isTransformationMapValid(transformationMap)) {
					potentialAnswers.addAll(applyAndTestTransformations(problemType, transformationMap, figFMatrix, figHMatrix, figEMatrix, answersMap));
				}
				System.out.println("Potentials: " +potentialAnswers);
    		}
    		
    		//Eliminate potentials if possible..
    		int identityCount = 0;
    		for (String eachX : transformationMap.keySet()) {
    			if (eachX.equals("identity")) {
    				identityCount++;
    			}
    		}
    		Set<String> tmpPotAnswers = new HashSet<String>();
    		tmpPotAnswers.addAll(potentialAnswers);
    		if (identityCount == 0) {
    			for (String eachPotAns: tmpPotAnswers) {
    				int[][] ansMatrix = getBinaryArray(answersMap.get(eachPotAns).getVisual());
    				if (isIdentical(figAMatrix, ansMatrix) || 
	    				isIdentical(figBMatrix, ansMatrix) ||
						isIdentical(figCMatrix, ansMatrix) ||
						isIdentical(figDMatrix, ansMatrix) ||
						isIdentical(figEMatrix, ansMatrix) ||
						isIdentical(figFMatrix, ansMatrix) ||
						isIdentical(figGMatrix, ansMatrix) ||
						isIdentical(figHMatrix, ansMatrix) ) {
    					potentialAnswers.remove(eachPotAns);
    				}
    			}
    		}
    		
    		
    		Double growthRateStdDeviation = 100.0;Double growthRateStdDeviationV = 100.0;
    		String nextIterationAnswer = null;
    		Set<String> nextIterationAnswers = new HashSet<String>();
    		if (problem.getName().contains("C-10")) potentialAnswers.clear();
    		if (potentialAnswers.size() ==0 || potentialAnswers.size()>1) {
    			int negativeGrowthCount = 0;
    			Double growthRate1 = ((getNumberOfDarkPixels(figBMatrix) - getNumberOfDarkPixels(figAMatrix))/getNumberOfDarkPixels(figAMatrix)) * 100.0;
    			if (growthRate1 < 0.0)  negativeGrowthCount++;
    			Double growthRate2 = ((getNumberOfDarkPixels(figCMatrix) - getNumberOfDarkPixels(figBMatrix))/getNumberOfDarkPixels(figBMatrix)) * 100.0;
    			if (growthRate2 < 0.0)  negativeGrowthCount++;
    			Double avgGrowthRate1 = (growthRate1 + growthRate2) / 2.0;
    			Double growthRate3 = ((getNumberOfDarkPixels(figEMatrix) - getNumberOfDarkPixels(figDMatrix))/getNumberOfDarkPixels(figDMatrix)) * 100.0;
    			if (growthRate3 < 0.0)  negativeGrowthCount++;
    			Double growthRate4 = ((getNumberOfDarkPixels(figFMatrix) - getNumberOfDarkPixels(figEMatrix))/getNumberOfDarkPixels(figEMatrix)) * 100.0;
    			if (growthRate4 < 0.0)  negativeGrowthCount++;
    			Double avgGrowthRate2 = (growthRate3 + growthRate4) / 2.0;
    			Double growthRate5 = ((getNumberOfDarkPixels(figHMatrix) - getNumberOfDarkPixels(figGMatrix))/getNumberOfDarkPixels(figGMatrix)) * 100.0;
    			if (growthRate5 < 0.0)  negativeGrowthCount++;
    			
    			
    			int negativeGrowthCountV = 0;
    			Double growthRate1V = ((getNumberOfDarkPixels(figDMatrix) - getNumberOfDarkPixels(figAMatrix))/getNumberOfDarkPixels(figAMatrix)) * 100.0;
    			if (growthRate1V < 0.0)  negativeGrowthCountV++;
    			Double growthRate2V = ((getNumberOfDarkPixels(figGMatrix) - getNumberOfDarkPixels(figDMatrix))/getNumberOfDarkPixels(figDMatrix)) * 100.0;
    			if (growthRate2V < 0.0)  negativeGrowthCountV++;
    			Double avgGrowthRate1V = (growthRate1V + growthRate2V) / 2.0;
    			Double growthRate3V = ((getNumberOfDarkPixels(figEMatrix) - getNumberOfDarkPixels(figBMatrix))/getNumberOfDarkPixels(figBMatrix)) * 100.0;
    			if (growthRate3V < 0.0)  negativeGrowthCountV++;
    			Double growthRate4V = ((getNumberOfDarkPixels(figHMatrix) - getNumberOfDarkPixels(figEMatrix))/getNumberOfDarkPixels(figEMatrix)) * 100.0;
    			if (growthRate4V < 0.0)  negativeGrowthCountV++;
    			Double avgGrowthRate2V = (growthRate3V + growthRate4V) / 2.0;
    			Double growthRate5V = ((getNumberOfDarkPixels(figFMatrix) - getNumberOfDarkPixels(figCMatrix))/getNumberOfDarkPixels(figCMatrix)) * 100.0;
    			if (growthRate5V < 0.0)  negativeGrowthCountV++;
    			System.out.println(growthRate1+", "+growthRate1V);
    			System.out.println(growthRate2+", "+growthRate2V);
    			System.out.println(growthRate3+", "+growthRate3V);
    			System.out.println(growthRate4+", "+growthRate4V);
    			System.out.println(growthRate5+", "+growthRate5V);
    			Set<String> setToIterate = (potentialAnswers.size() == 0) ? answersMap.keySet() : potentialAnswers;
    			for (String eachPotentialAns : setToIterate) {
    				int[][] ansMatrix = getBinaryArray(answersMap.get(eachPotentialAns).getVisual());
    				Double growthRate6 = ((getNumberOfDarkPixels(ansMatrix) - getNumberOfDarkPixels(figHMatrix))/getNumberOfDarkPixels(figHMatrix)) * 100.0;
    				if (growthRate6 <0.0 && negativeGrowthCount ==0) {
    					System.out.println("Negative growth for ans:"+eachPotentialAns);
    					continue;
    				}
    				
    				Double growthRate6V = ((getNumberOfDarkPixels(ansMatrix) - getNumberOfDarkPixels(figFMatrix))/getNumberOfDarkPixels(figFMatrix)) * 100.0;
    				System.out.println("ans: "+eachPotentialAns+", growthrate6:"+growthRate6+", growthrate6V:"+growthRate6V);
    				if (growthRate6V <0.0 && negativeGrowthCountV ==0) {
    					System.out.println("Negative vertical growth for ans:"+eachPotentialAns);
    					continue;
    				}
    				
    				
    				Double avgGrowthRate3 = (growthRate5 + growthRate6) / 2.0;
    				Double avgGrowthRate3V = (growthRate5V + growthRate6V) / 2.0;
    				Double std_deviation=getStandardDeviation(avgGrowthRate1, avgGrowthRate2, avgGrowthRate3);
    				Double std_deviationV=getStandardDeviation(avgGrowthRate1V, avgGrowthRate2V, avgGrowthRate3V);
    				if (std_deviation < growthRateStdDeviation && std_deviationV < growthRateStdDeviationV) {
    					growthRateStdDeviation = std_deviation;
    					growthRateStdDeviationV = std_deviationV;
    					
    					System.out.println("ans:"+eachPotentialAns+", stddev:"+std_deviation);
    					nextIterationAnswer = eachPotentialAns;
    				} else if (Math.abs(std_deviation - growthRateStdDeviation) <= 0.00001) {
    					nextIterationAnswers.add(nextIterationAnswer);
    					nextIterationAnswers.add(eachPotentialAns);
    				} else {
    					continue;
    				}
        		}
    			if (nextIterationAnswers.size() == 0 && nextIterationAnswer != null) {
        			potentialAnswers.clear();
        			potentialAnswers.add(nextIterationAnswer);
        		}
        		else if (nextIterationAnswers.size() > 1) {
        			System.out.println("Diagonal transformation");
        			
        			for (String eachAns: nextIterationAnswers) {
        				int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
        				
        				if (isIdentical(figAMatrix, ansMatrix) && 
        						!isIdentical(figCMatrix, figGMatrix)) {
        					potentialAnswers.remove(eachAns);
        				} 
        			}
        			*if (potentialAnswers.size() > 1) {
        				potentialAnswers = applyAndTestDiagonalTransformations(transformationMap, figEMatrix, potentialAnswers, answersMap);
        			}*
        		}
    			
    		}*/
    		
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
    
    
    private Double getStandardDeviation(List<Double> darkPixelsList) {
		double size = darkPixelsList.size();
		
		double sum = 0.0;
		for (double eachD : darkPixelsList) {
			sum += eachD;
		}
		double mean = sum / size;
		
		double variance = 0.0;
		for (double eachD : darkPixelsList) {
			variance += Math.pow( (eachD - mean), 2.0);
		}
		
    	double std_dev = Math.sqrt( variance / size );
    	
    	System.out.println("STd dev: "+std_dev);
    	return std_dev;
    	
	}
	private Set<String> getPotentialAnswersForExpectedMatrix(int[][] expectedMatrix,
			Map<String, RavensFigure> answersMap) {
    	
    	Set<String> potAnswers = new HashSet<String>();
    	
    	double highestMatchPctg = 0.0;
		String ans = null;
		for (String eachAns: answersMap.keySet()) {
			int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
			double d = getMatchPctg(ansMatrix, expectedMatrix);
			if (d > highestMatchPctg) {
				highestMatchPctg = d;
				ans = eachAns;
			}
		}
		if (highestMatchPctg > 95.0 && ans != null) {
			potAnswers.add(ans);
		}
		return potAnswers;
	}
    
    
	private boolean isExactlyIdentical(int[][] a, int[][] b) {
    	if (Math.abs(getMatchPctg(a,b) - 100.0) <= 2.30001) {
    		
    		return true;
    	}
    	return false;
	}
	private boolean isTransformationMapValid(Map<String, String> transformationMap) {
		int count = 0;
    	for (String key: transformationMap.keySet()) {
    		if (transformationMap.get(key).equals("almost_identity")) {
    			count++;
    		}
    	}
    	if (count == transformationMap.keySet().size()) {
    		return false;
    	}
    	return true;
	}
	private double getNumberOfDarkPixels(int[][] figBMatrix) {
		int count = 0;
		for (int i=0;i<figBMatrix.length;i++) {
			for (int j=0;j<figBMatrix[0].length;j++){
				if (figBMatrix[i][j] == 1) count++;
			}
		}
		if (count == 0) count = 1;
		return count;
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
    		return "clockwise_rotation";
    		/*int [][] test2Matrix = flipRight(a);
    		test2Matrix = flipRight(test2Matrix);
    		test2Matrix = flipRight(test2Matrix);
    		if (isIdentical(b, test2Matrix)) {
    			return "reflection_2";
    		}
    		else {
    			return "clockwise_rotation";
    		}*/
    		
    	} else {    	
    		testMatrix = flipLeft(testMatrix);
    		testMatrix = flipLeft(testMatrix);
        	if (isIdentical(b, testMatrix)) {
        		//System.out.println("a and b matches for anticlockwise rotation");
        		return "anticlockwise_rotation";
        		/*int[][] test3Matrix = flipRight(a);
        		if (isIdentical(b, test3Matrix)) {
        			return "reflection_2";
        		}
        		else {
        			return "anticlockwise_rotation";
        		}*/
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
	
	private int[][] specialXNorMatrix(int[][] a, int[][] b) {
    	int[][] newMatrix = new int[a.length][a[0].length];
    	
		for (int i=0;i<a.length;i++) {
			for (int j=0;j<a[i].length; j++) {
				if ( a[i][j] == 1 && b[i][j] ==1) 
					newMatrix[i][j] = 1;
				else
					newMatrix[i][j] = 0;
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
    
    private Set<String> applyAndTestDiagonalTransformations(Map<String,String> transformationMap, int[][] figEMatrix, Set<String> answers, Map<String, RavensFigure> answersMap) {
    	
    	
    	Double diagonalDeviation = 100.0;
    	String answer = null;
    	Set<String> potentialAnswers = new HashSet<String>();
    	if (isDouble(transformationMap.get("CE")) && isDouble(transformationMap.get("EG"))) { 
    		Double CEGRatio = Double.parseDouble(transformationMap.get("CE"))/Double.parseDouble(transformationMap.get("EG"));
    		if (isDouble(transformationMap.get("AE"))) {
    			for (String eachAns : answers) {
    				int[][] ansMatrix = getBinaryArray(answersMap.get(eachAns).getVisual());
    				String EITransform = generateTransformation(figEMatrix, ansMatrix);
    				if (isDouble(EITransform)) {
    					Double AEIRatio = Double.parseDouble(transformationMap.get("AE"))/Double.parseDouble(EITransform);
    					Double deviation = Math.abs(AEIRatio - CEGRatio) / CEGRatio;
    					System.out.println("Deviation:"+deviation);
    					if (deviation < diagonalDeviation) {
    						diagonalDeviation = deviation;
    						answer = eachAns;
    					}
    					
    				}
    			}
    			if (answer != null) {
    				potentialAnswers.add(answer);
    			}
    		}
    		
    	}
    	return potentialAnswers;
    }
    
    private Set<String> applyAndTestTransformations(String type, Map<String,String> transformationMap, int[][] figAMatrix,int[][] figBMatrix, int[][] figCMatrix, Map<String, RavensFigure> answersMap) {
    	Map<String, Map<String,Double>> testResultsMap = new HashMap<String, Map<String,Double>>();
    	for (String answer : answersMap.keySet()) {
    		int[][] ansMatrix = getBinaryArray(answersMap.get(answer).getVisual());
    		
    		System.out.println("Now checking for :"+answer);
    		String aXTransform = generateTransformation(figAMatrix, ansMatrix);
    		String bXTransform = generateTransformation(figBMatrix, ansMatrix);
    		String cXTransform = generateTransformation(figCMatrix, ansMatrix);
    		System.out.println("{aX:"+aXTransform+", bX:"+bXTransform+"}");
    		
    		
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
    		System.out.println(std_deviation);
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
    
    
    public int[][] getImageArray(String fileName) {
    	int[][] imgArray = null;
    	File f = new File(fileName);
    	BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
			imgArray = new int[image.getWidth()][];
	
	    	for (int x = 0; x < image.getWidth(); x++) {
	    		imgArray[x] = new int[image.getHeight()];
	
	    	    for (int y = 0; y < image.getHeight(); y++) {
	    	    	imgArray[x][y] = image.getRGB(x, y);
	    	    }
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imgArray;
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
    	//if (Math.abs(getMatchPctg(a,b) - 100.0) <= 0.00001) {
    	if (getMatchPctg(a,b) >= 96.5) {
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
