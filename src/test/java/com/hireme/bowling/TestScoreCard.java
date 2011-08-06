package com.hireme.bowling;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * JUnit4 Parameterized test case for @see ScoreCard.class. JUnit will run all
 * tests for each data parameter specified
 * 
 * @author Jim Klucar
 * 
 */
@RunWith(value = Parameterized.class)
public class TestScoreCard {

	private int[] ballScores;
	private int totalScore;

	/**
	 * Constructor from TestData parameter
	 * 
	 * @param testData
	 *            - instance of inner class that holds data for a single test
	 *            run.
	 */
	public TestScoreCard(TestData testData) {
		this.ballScores = testData.ballScores;
		this.totalScore = testData.totalScore;
	}

	/**
	 * Container class to hold data for a single test.
	 * 
	 */
	public static class TestData {
		final int totalScore;
		final int[] ballScores;

		/**
		 * New TestData object
		 * 
		 * @param totalScore
		 *            - score for the ballScores array
		 * @param ballScores
		 *            - array specifying the number of pins knocked down with
		 *            each throw.
		 */
		public TestData(int totalScore, int[] ballScores) {
			this.totalScore = totalScore;
			this.ballScores = ballScores;
		}
	}

	/**
	 * JUnit4 data parameter definition
	 * 
	 * @return Object collection containing a TestData object for each test case
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] testData = new Object[][] {
				// all gutter balls
				{ new TestData(0, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				// perfect game
				{ new TestData(300, new int[] { 10, 10, 10, 10, 10, 10, 10, 10,
						10, 10, 10, 10 }) },
				// no strikes or spares
				{ new TestData(90, new int[] { 4, 5, 5, 4, 3, 6, 2, 7, 0, 9, 6,
						3, 8, 1, 1, 8, 9, 0, 7, 2 }) },
				// strike on 9th frame
				{ new TestData(100, new int[] { 4, 5, 5, 4, 3, 6, 2, 7, 0, 9,
						6, 3, 8, 1, 1, 8, 10, 7, 2 }) },
				{ new TestData(82, new int[] { 4, 5, 5, 4, 3, 6, 2, 7, 0, 9, 6,
						3, 8, 1, 1, 8, 10, 0, 0, 0 }) },
				// spare on 9th frame
				{ new TestData(14, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 1, 9, 2, 0 }) },
				{ new TestData(15, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 1, 9, 2, 1 }) },
				// strike in middle frame
				{ new TestData(10, new int[] { 0, 0, 0, 0, 10, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				{ new TestData(12, new int[] { 0, 0, 0, 0, 10, 1, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				{ new TestData(14, new int[] { 0, 0, 0, 0, 10, 1, 1, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				// spare in middle frame
				{ new TestData(10, new int[] { 0, 0, 0, 0, 1, 9, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				{ new TestData(12, new int[] { 0, 0, 0, 0, 1, 9, 1, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				{ new TestData(13, new int[] { 0, 0, 0, 0, 1, 9, 1, 1, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				// strikes in 10th frame
				{ new TestData(10, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 10 }) },
				{ new TestData(50, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 10, 10, 10 }) },
				// spare in 10th frame
				{ new TestData(10, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 9, 1 }) },
				// spare first frame
				{ new TestData(22, new int[] { 6, 4, 5, 2, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) },
				// strike first frame
				{ new TestData(31, new int[] { 10, 5, 2, 7, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0 }) }, };
		return Arrays.asList(testData);
	}

	/**
	 * Creates a ScoreCard object from the specified TestData
	 * and verifies it calculates the correct score.
	 * 
	 * @see ScoreCard
	 */
	@Test
	public void testScoreCard() {
		ScoreCard scoreCard = new ScoreCard(ballScores);
		int score = scoreCard.getScore();
		assertEquals(createFailureMessage(score), totalScore, score);
	}

	private String createFailureMessage(int score) {
		StringBuilder failMsg = new StringBuilder();
		failMsg.append("Test Failure: ");
		for (int ii = 0; ii < ballScores.length; ii++) {
			failMsg.append(ballScores[ii]).append(" ");
		}
		failMsg.append(System.getProperty("line.separator"));
		failMsg.append("Expected: ").append(totalScore);
		failMsg.append(" Calculated: ").append(score);

		return failMsg.toString();
	}

}
