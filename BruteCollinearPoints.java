/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    // Number of items in segments
    private int n = 0;

    // create a doubling array
    private LineSegment[] segments = new LineSegment[1];

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument is null");
        }
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException("point cannot be null");
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("contain repeated points");
            }
        }
        // finds all line segments containing 4 points
        for (int a = 0; a < points.length; a++) {
            for (int b = a + 1; b < points.length; b++) {
                for (int c = b + 1; c < points.length; c++) {
                    for (int d = c + 1; d < points.length; d++) {
                        // For each combination of points,
                        Point[] combination = { points[a], points[b], points[c], points[d] };

                        double pq = combination[0].slopeTo(combination[1]);
                        double pr = combination[0].slopeTo(combination[2]);
                        double ps = combination[0].slopeTo(combination[3]);

                        if (pq == pr && pq == ps) {
                            Point largest = combination[0];
                            Point smallest = combination[0];
                            for (int i = 1; i < combination.length; i++) {
                                if (combination[i].compareTo(largest) > 0) {
                                    largest = combination[i];
                                }
                                if (combination[i].compareTo(smallest) < 0) {
                                    smallest = combination[i];
                                }
                            }
                            push(new LineSegment(smallest, largest));
                        }

                    }
                }
            }
        }
    }

    private void push(LineSegment segment) {
        if (n == segments.length) {
            resize(2 * segments.length);
        }
        segments[n++] = segment;
    }

    private void resize(int length) {
        LineSegment[] copy = new LineSegment[length];
        for (int i = 0; i < segments.length; i++) {
            copy[i] = segments[i];
        }
        segments = copy;
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[n];
        for (int i = 0; i < n; i++) {
            copy[i] = segments[i];
        }
        return copy;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
