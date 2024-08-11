
package renderer;

/**
 * Pixel is a helper class. It is used for multi-threading in the renderer and
 * for follow up its progress.<br/>
 * There is a main follow up object and several secondary objects - one in each
 * thread.
 *
 * @author Dan
 *
 */
class Pixel {
    /** Maximum rows of pixels */
    private static int maxRows = 0;
    /** Maximum columns of pixels */
    private static int maxCols = 0;
    /** Total amount of pixels in the generated image */
    private static long totalPixels = 0l;
    /** Currently processed row of pixels */
    private static volatile int cRow = 0;
    /** Currently processed column of pixels */
    private static volatile int cCol = -1;
    /** Amount of pixels that have been processed */
    private static volatile long pixels = 0l;
    private static volatile long last = -1l;
    /** Last printed progress update percentage */
    private static volatile int lastPrinted = -1;
    /** Flag of debug printing of progress percentage */
    private static boolean print = false;
    /** Progress percentage printing interval */
    private static long printInterval = 100l;
    /** Printing format */
    private static final String PRINT_FORMAT = "%5.1f%%\r";
    /** Mutual exclusion object for synchronizing next pixel allocation between threads */
    private static Object mutexNext = new Object();
    /** Mutual exclusion object for printing progress percentage in console window
     * by different threads */
    private static Object mutexPixels = new Object();

    int row;
    int col;

    /**
     * Initialize pixel data for multi-threading
     *
     * @param maxRows  the amount of pixel rows
     * @param maxCols  the amount of pixel columns
     * @param interval print time interval in seconds, 0 if printing is not required
     */
    static void initialize(int maxRows, int maxCols, double interval) {
        Pixel.maxRows = maxRows;
        Pixel.maxCols = maxCols;
        Pixel.totalPixels = (long) maxRows * maxCols;
        cRow = 0;
        cCol = -1;
        pixels = 0;
        printInterval = (int) (interval * 1000);
        print = printInterval != 0;
        last = -1l;
    }

    /**
     * Function for thread-safe manipulating of main follow up Pixel object - this
     * function is critical section for all the threads, and static data is the
     * shared data of this critical section.<br/>
     * The function provides next available pixel number each call.
     *
     * @return true if next pixel is allocated, false if there are no more pixels
     */
    public boolean nextPixel() {
        synchronized (mutexNext) {
            if (cRow == maxRows)//the current row  has reached the maximum number of rows
                return false;//there are no more pixels to allocate
            ++cCol;//moving to the next pixel in the current row
            if (cCol < maxCols) {
                row = cRow;
                col = cCol;
                return true;//indicating that a new pixel has been allocated.
            }
            cCol = 0;
            ++cRow;//moving to the next row
            if (cRow < maxRows) {
                row = cRow;
                col = cCol;
                return true;
            }
            return false;
        }
    }

    /**
     * Finish pixel processing
     */
    static void pixelDone() {
        synchronized (mutexPixels) {//This ensures that only one thread at a time can execute this block of code
            ++pixels;
        }
    }

    /**
     * Wait for all pixels to be done and print the progress percentage - must be
     * run from the main thread
     */
    public static void waitToFinish() {
        if (print)
            System.out.printf(PRINT_FORMAT, 0d);//print initial progress (0%)

        while (last < totalPixels) {//the number of processed pixels is less than the total number of pixels to be processed
            printPixel();
            try {
                Thread.sleep(printInterval);
            } catch (InterruptedException ignore) {
                if (print)
                    System.out.print("");
            }
        }
        if (print)
            System.out.println("100.0%");
    }

    /**
     * Print pixel progress percentage
     */
    public static void printPixel() {
        long current = pixels;
        if (print && last != current) {// progress should be printed
            int percentage = (int) (1000l * current / totalPixels);//This calculates the progress percentage. The current number of processed pixels is multiplied by 1000
            //and then divided by the total number of pixels
            if (lastPrinted != percentage) {//ensures that the progress is only printed if it has changed since the last update.
                last = current;
                lastPrinted = percentage;
                System.out.println( percentage / 10d + "%");
            }
        }
    }
}