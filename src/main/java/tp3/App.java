package tp3;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator(2);
        gen.addSink(graph);
        gen.begin();
        for(int i=0; i<100; i++)
            gen.nextEvents();
        gen.end();
        graph.display();
    }
}
