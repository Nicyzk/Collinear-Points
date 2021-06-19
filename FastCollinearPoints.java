/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    // Number of items in segments
    private int n = 0;

    // create a doubling array
    private LineSegment[] segments = new LineSegment[1];

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
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

        for (int i = 0; i < points.length - 1; i++) {
            // Treat p (copy[i]) as origin
            Point p = points[i];

            // Use sub-array of points after p i.e. copy[i+1 to N].
            // Why sort ONLY with points AFTER p? !!! Still not enough. Did not account for subset combinations.
            // For current cycle, we will find combinations (with p BUT not points before p) that create a line segment
            // Combinations with p AND points before p that form a line segment have already been found in previous cycles.
            // Finding them again is double counting.
            // Sort sub-array using compareTo.

            Arrays.sort(points, i + 1, points.length);

            // sort sub-array using slopeOrder against p.
            Arrays.sort(points, i + 1, points.length, p.slopeOrder());
            // Loop through sub-array, if slopeTo equal 3 or more times, create LineSegment with first and last element.
            int count = 1;
            int first = i + 1;
            int last = i + 1;

            double currentSlope = points[first].slopeTo(p);
            for (int j = i + 2; j < points.length; j++) {
                double nextSlope = points[j].slopeTo(p);
                if (currentSlope == nextSlope) {
                    count++;
                    last = j;
                }
                if (j == points.length - 1 || currentSlope != nextSlope) {
                    if (count >= 3) {
                        // To prevent the same line segments from being pushed again due to subset combinations in subsequent cycles
                        boolean inserted = false;
                        for (int k = 0; k < i; k++) {
                            if (points[k].slopeTo(p) == points[first].slopeTo(points[last])) {
                                inserted = true;
                            }
                        }
                        if (!inserted) {
                            // rmb that p is also part of combination
                            if (p.compareTo(points[first]) < 0)
                                push(new LineSegment(p, points[last]));
                            else if (p.compareTo(points[last]) > 0)
                                push(new LineSegment(points[first], p));
                            else push(new LineSegment(points[first], points[last]));
                        }
                    }
                    first = j;
                    last = j;
                    currentSlope = points[first].slopeTo(p);
                    count = 1;
                }
            }
        }
        // Repeat the cycle, using new origin.
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
        // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
