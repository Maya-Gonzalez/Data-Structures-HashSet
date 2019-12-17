public class SimpleQuadraticProbe implements ProbeStrategy {

    public int probe(int arraySize, int originalHash, int attemptNum) {
		return (originalHash + (attemptNum * attemptNum)) % arraySize ;
	
    }

}