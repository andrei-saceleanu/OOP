package my_pair;
/**
 * An implementation of 2-sized tuple of integers,similar with pair from C/C++
 * @author Saceleanu Andrei
 *
 */
public class MyPair implements Comparable<MyPair>{
	private int first,second;
	
	/**
	 * Default constructor
	 */
	public MyPair() {}
	/**
	 * Constructor with parameters
	 * @param first first element in pair
	 * @param second second element in pair
	 */
	public MyPair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	
	/**
	 * @return first element in pair
	 */
	public int getFirst() {
		return first;
	}
	/**
	 * @param first	first element in pair
	 */
	public void setFirst(int first) {
		this.first = first;
	}
	/**
	 * @return second element in pair
	 */
	public int getSecond() {
		return second;
	}
	/**
	 * @param second second element in pair
	 */
	public void setSecond(int second) {
		this.second = second;
	}
	@Override
	/**
	 * compareTo overriden for pairs.
	 * The elements are compared component by component.
	 */
	public int compareTo(MyPair arg0) {
		// TODO Auto-generated method stub
		if(this.first>arg0.first) {
			return 1;
		}else if(this.first<arg0.first) {
			return -1;
		}else {
			if(this.second>arg0.second) {
				return 1;
			}else if(this.second<arg0.second) {
				return -1;
			}
			return 0;
		}
	}
	
}