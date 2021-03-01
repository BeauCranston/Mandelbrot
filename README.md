# Mandelbrot


Draws a Mandelbrot image using math. The calculations are done on multiple background threads, while the drawing is being handled by the Java FX application layer. This was done using a ConncurrentLinkedQueue where the application thread listens on the datastructure for new additions, while multiple threads populates the ConcurrentLinkedQueue with 'Paint Data'.
