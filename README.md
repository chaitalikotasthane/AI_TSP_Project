Traveling Salesman Problem
Given a set of cities and the distance between each pair of cities, it basically aims to find the minimum cost/ shortest possible route required to traverse all cities exactly once and return back to the starting point.

We use the following concepts to approximate the solution for this problem:

1. Branch and Bound DFS
2. Stochastic Local Search
    - Simulated Annealing
    - Farthest Insertion
    - 2-Opt

To run:

1. Clone the source code or download zip
2. Run Main.class
3. Enter test file name to test on (From the generated test files with different number of cities and costs)
4. Choose one of the two approaches Branch and Bound DFS or Stochastic Local Search

Output generated per approach:

1. Branch and Bound DFS

	Minimum cost and its corresponding path

2. Stochastic Local Search
	
	Number of iterations
	Current tour
	Updated tour after 2-Opt
	Best cost so far and its corresponding tour
