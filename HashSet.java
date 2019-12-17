
public class HashSet {

	private int[] values;
	private int size = 0;
	private int ratio;
	private int attemptNum;
	private int index; 

	private ProbeStrategy strategy;

	public HashSet(int initArrayLength, int ratio, ProbeStrategy strategy) {
		values = new int[initArrayLength]; // makes new array, size at construction time
		for (int i = 0; i < values.length; i++) {
			values[i] = -1;
		}
		this.ratio = ratio; // max allowed elements before resizing
		this.strategy = strategy;
	}
	
	int size() {
		return size;
	}

	public void resize() {
		int[] temp = new int[(values.length * 2) + 1];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = -1;
		}

		int[] newValues = values;
		values = temp;

		for (int index = 0; index < newValues.length; index++) {
			if (newValues[index] > 0) { // occupied 
				int value = newValues[index]; // occupied value
				// calculate new index to add at
				int modIndex = value % values.length;
				if (values[modIndex] < 0) { // NO COLLISION
					values[modIndex] = newValues[index];
				} else if (values[modIndex] > 0) { // COLLISION
					modIndex = hash(values[modIndex]);
					values[modIndex] = newValues[index];
				}
			}
		}
	}

	private int hash(int value) {
		index = value % values.length;
		attemptNum = 0;
		while (attemptNum < values.length) {
			if (values[index] < 0) {
				break;
			}
			index = strategy.probe(values.length, value, attemptNum);
			attemptNum++;
		}
		return index;
	}

	public boolean add(int value) {
		if (value < 0) { // only continue if value is positive
			return false;
		}
		if (contains(value)) { // value already in the array, don't do anything
			return false;
		}

		// calculate new hash value for index
		index = value % values.length;
		while (values[index] > 0) { // index is occupied, collision
			// hash again, need to find new index
			index = hash(value);
			attemptNum++;

		}
		if (values[index] < 0) {
			values[index] = value;
			size++;
		}
		
		if (values.length < ratio * size) { // condition for resize
			resize();
		}
		index = strategy.probe(values.length, value, attemptNum);
		attemptNum++;
		return true;
	}

	public boolean contains(int value) {
		index = value % values.length;
		attemptNum = 0;

		while (values[index] != -1 && attemptNum < values.length) {
			if (values[index] > 0 && values[index] == value) {
				return true;
			}
			index = strategy.probe(values.length, value, attemptNum);
			attemptNum++;
		}

		return false;
	}

	public boolean remove(int value) {
		if (!contains(value)) {
			return false;
		}
		index = value % values.length;
		attemptNum = 0;
		while ((values[index] != -1) && (attemptNum < values.length)) { // index is occupied and all buckets have not yet been probed
			if ((values[index] > 0) && (values[index] == value)) { 
				values[index] = -2; // -2 indicates empty after removal
				size--;
				return true;
			}
			index = strategy.probe(values.length, value, attemptNum);
			attemptNum++;

		}
		return false;
	}

	public int[] toArray() {
		int[] result = new int[this.values.length];
		for (int i = 0; i < this.values.length; i++) {
			result[i] = this.values[i];
		}
		return result;
	}

	public static void main(String[] args) {
		ProbeStrategy linear = new SimpleLinearProbe();
		HashSet set = new HashSet(7, 3, linear);

		int[] numbers = { 20, 5, 3, 9 };
		for (int i = 0; i < numbers.length; i++) {
			// add the number
			set.add(numbers[i]);
			// print out the array
			int[] array = set.toArray();
			for (int j = 0; j < array.length; j++) {
				System.out.print(array[j] + ", ");
			}
			System.out.println();
		}
	}
}