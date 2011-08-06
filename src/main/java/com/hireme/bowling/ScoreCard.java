package com.hireme.bowling;

import org.ejml.simple.SimpleMatrix;

/**
 * Calculates the score of a game of bowling, given an array that represents the
 * number of pins knocked down for each ball thrown.
 * <p>
 * Algorithm: 
 * Generates an upper diagonal matrix "A" that can be used to quickly
 * generate the bowler's final score. If the bowler throws 15 balls in a game,
 * "A" will be a 15x15 matrix, and F is a 15x1 matrix containing the number of
 * pins knocked down with each throw. The matrices are mutiplied with a 1x15
 * matrix "J" consisting of all ones. The final score is the product JxAxF.
 * </p>
 * <p>
 * For Example, a strike gets a bonus for the next two balls thrown. If those 
 * throws knock a single pin down, the calculation would be:
 * <p>
 * <table>
 *   <tr>
 *   <td>
 *     <table><tr><td></td></tr><tr><td>[1 1 1]</td></tr></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr><td></td></tr><tr>*</tr><tr><td></td></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr><td>[1 1 1]</td></tr><tr><td>[0 1 0]</td></tr><tr><td>[0 0 1]</td></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr><td></td></tr><tr>*</tr><tr><td></td></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr><td>[10]</td></tr><tr><td>[1]</td></tr><tr><td>[1]</td></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr><td></td></tr><tr>=</tr><tr><td></td></tr></table>
 *   </td>    
 *   <td>
 *     <table><tr/><tr>[14]</tr><tr/></table>
 *   </td>    
 *   
 *   <tr>        
 * </table>
 *           
 * </p>
 * <p>
 * The logic is just extended to a matrix sized to the number of balls thrown.
 * </p>
 * 
 * Assumptions:
 * <ul> 
 * <li>Array is a valid bowling game.</li> 
 * <li>Array represents only the balls thrown, 
 *    so a strike is represented as 10 not 10,0</li>
 * </ul>
 * Future Considerations:
 * <ul>
 * <li>The A matrix could be generated dynamically during a game.</li>
 * <li>A*F gives a running total of the scores, could expose this
 * if dynamic scoring is desired.</li>
 * <li>Different games could be created by adjusting various constants
 * (i.e. 15 frame game, 3 ball bonus for a strike, etc)</li>
 * </ul>
 * <p></p>
 * 
 * @author Jim Klucar
 * 
 */
public class ScoreCard {

//	 *           [1 1 1]   [10]
//	                        * [1 1 1] * [0 1 0] * [1]  = [14]
//	                        *           [0 0 1]   [1]

	private static final int MAX_PINS_PER_FRAME = 10;
	private static final int TOTAL_FRAMES = 10;
	private static final int BONUS_FRAMES_PER_STRIKE = 2;
	private static final int BONUS_FRAMES_PER_SPARE = 1;

	private SimpleMatrix scoreVector;
	private SimpleMatrix scoreMatrix;
	private SimpleMatrix onesVector;
	private int numThrows;

	/**
	 * Construct a ScoreCard from a score array.
	 * 
	 * @param scores
	 *            Represents the number of pins knocked down for each ball
	 *            thrown. Assumptions defined in the class overview. 
	 */
	public ScoreCard(int[] scores) {
		initializeCalculation(scores);
		populateScoreMatrix();
	}

	private void initializeCalculation(int[] scores) {
		numThrows = scores.length;
		scoreMatrix = SimpleMatrix.identity(numThrows);
		scoreVector = new SimpleMatrix(numThrows, 1);
		onesVector = new SimpleMatrix(1, numThrows);
		// populate the vectors
		for (int ii = 0; ii < numThrows; ii++) {
			scoreVector.set(ii, 0, scores[ii]);
			onesVector.set(0, ii, 1);
		}
	}

	// populate the score matrix from the scores array
	private void populateScoreMatrix() {
		double frameScore = 0;
		for (int ballIdx = 0, frameCount = 0; (ballIdx < numThrows)
				&& (frameCount < TOTAL_FRAMES - 1); ballIdx++, frameCount++) {
			frameScore = scoreVector.get(ballIdx, 0);
			if (frameScore == MAX_PINS_PER_FRAME) {
				// strike, set next two balls
				addStrikeBonus(ballIdx);
			} else if (frameScore + scoreVector.get(ballIdx + 1, 0) == MAX_PINS_PER_FRAME) {
				// spare, skip next ball
				addSpareBonus(++ballIdx);
			} else {
				// otherwise frame was open, no bounus, skip to next frame
				ballIdx++;
			}
		}
	}

	// add the additional ones into the scoreMatrix for numBonusFrames
	private void addBonus(int ballIdx, int numBonusFrames) {
		for (int bb = 1; bb <= numBonusFrames; bb++) {
			int bonusIdx = ballIdx + bb;
			if (bonusIdx < numThrows) {
				scoreMatrix.set(ballIdx, bonusIdx, 1);
			}
		}
	}

	private void addStrikeBonus(int ballIdx) {
		addBonus(ballIdx, BONUS_FRAMES_PER_STRIKE);
	}

	private void addSpareBonus(int ballIdx) {
		addBonus(ballIdx, BONUS_FRAMES_PER_SPARE);
	}

	/**
	 * Calculates the final score of the game.
	 * 
	 * @return the score for game.
	 * 
	 */
	public int getScore() {
		SimpleMatrix matScore = onesVector.mult(scoreMatrix).mult(scoreVector);
		double dScore = matScore.get(0, 0);
		return (int) dScore;
	}

}
