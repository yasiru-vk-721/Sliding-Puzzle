/* w1985681, 20221190
 * Yasiru Katuwandeniya*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SlidingPuzzle {
    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    static boolean isValid(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    public static List<Point> shortestPathSearch(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        int[][] parentX = new int[m][n];
        int[][] parentY = new int[m][n];

        Queue<Point> queue = new LinkedList<>();
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        // Find start and end points
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (matrix[i][j] == 'F') {
                    endX = i;
                    endY = j;
                }
            }
        }

        queue.offer(new Point(startX, startY));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Point currentPoint = queue.poll();
            int x = currentPoint.x;
            int y = currentPoint.y;

            if (x == endX && y == endY) {
                // Reconstruct path
                List<Point> slidingPath = new ArrayList<>();
                while (x != startX || y != startY) {
                    slidingPath.add(new Point(x, y));
                    int temp = parentX[x][y];
                    y = parentY[x][y];
                    x = temp;
                }
                slidingPath.add(new Point(startX, startY));
                Collections.reverse(slidingPath);
                return slidingPath;
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isValid(nx, ny, m, n) && matrix[nx][ny] != '0' && !visited[nx][ny]) {
                    queue.offer(new Point(nx, ny));
                    visited[nx][ny] = true;
                    parentX[nx][ny] = x;
                    parentY[nx][ny] = y;
                }
            }
        }

        // If no path found
        return new ArrayList<>();
    }

    static String printMoves(Point from, Point to) {
        if (from.x < to.x) return "Move down to (" + to.x + ", " + to.y + ")";
        if (from.x > to.x) return "Move up to (" + to.x + ", " + to.y + ")";
        if (from.y < to.y) return "Move right to (" + to.x + ", " + to.y + ")";
        if (from.y > to.y) return "Move left to (" + to.x + ", " + to.y + ")";
        return "Stay at (" + to.x + ", " + to.y + ")";
    }

    public static void BFSAlgorithmRun(String exampleFilePath) {
        long StartTime = System.currentTimeMillis();
        File file = new File(exampleFilePath);
        System.out.println("Processing file: " + file.getName());
        try {

            Scanner scanner = new Scanner(file);
            List<char[]> matrixList = new ArrayList<>();

            System.out.println("Input file: ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    if (!matrixList.isEmpty()) break;
                } else {
                    char[] row = line.toCharArray();
                    matrixList.add(row);
                    System.out.println(line); // Print each row of the matrix
                }
            }

            System.out.println();
            int rows = matrixList.size();
            int cols = matrixList.get(0).length;

            char[][] matrix = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                matrix[i] = matrixList.get(i);
            }

            List<Point> shortestPath = shortestPathSearch(matrix);
            if (shortestPath.isEmpty()) {
                System.out.println("No path found for file: " + file.getName());
            } else {
                System.out.println("Shortest path for file: " + file.getName());

                Point previous = null;
                for (Point point : shortestPath) {
                    if (previous != null) {
                        String move = printMoves(previous, point);
                        System.out.println(move);
                    } else {
                        System.out.println("Start at (" + point.x + ", " + point.y + ")");
                    }
                    previous = point;
                }
                System.out.println("Move right to (" + shortestPath.get(shortestPath.size() - 1).x
                        + ", " + shortestPath.get(shortestPath.size() - 1).y
                        + ") \nDone.");
            }

            scanner.close();
            long endTime = System.currentTimeMillis();
            long duration = endTime - StartTime;

            System.out.println("Runtime: "+ duration + " milliseconds." );
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Press 1 for maze files. \nPress 2 for benchmark files");
        System.out.print("Choice: ");
        int choice = scan.nextInt();
        System.out.println();

        switch (choice) {
            case 1 -> {
                // File path for maze. Change according to your need.
                String mazeFilePath = "examples/maze10_3.txt";
                BFSAlgorithmRun(mazeFilePath);
            }
            case 2 -> {
                // File path for benchmark. Change according to your need.
                String puzzleFilePath = "examples/puzzle_10.txt";
                BFSAlgorithmRun(puzzleFilePath);
            }
        }
        System.out.println();
        System.out.println("You have to change file path(only the number) in code line 166  and 171 to check other input files.(eg: puzzle_10 -> puzzle_20)");
    }
}
