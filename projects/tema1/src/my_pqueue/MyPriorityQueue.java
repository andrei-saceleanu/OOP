package my_pqueue;

/**
 * Generic implementation of a Priority Queue.
 * Internally,the elements are organized in a MinHeap in order to provide
 * proper functionality during Dijkstra subroutine.
 * @author Saceleanu Andrei
 *
 * @param <T> generic type which must be comparable/have a natural order between elements
 */
public class MyPriorityQueue<T extends Comparable<T>> {
	private T[] elements;
	private int size,capacity;
	/**
	 * Default constructor.
	 * With no parameters,the maximum size/capacity is set to 5000.
	 */
	@SuppressWarnings("unchecked")
	public MyPriorityQueue() {
		this.size=0;
		this.elements=(T [])new Comparable[5000];
		this.capacity=5000;
	}
	/**
	 * Constructor with parameters.
	 * @param capacity	maximum size of internal array of elements
	 */
	@SuppressWarnings("unchecked")
	public MyPriorityQueue(int capacity) {
		this.size=0;
		this.elements=(T [])new Comparable[capacity];
		this.capacity=capacity;
	}
	
	/**
	 * Adds an element to the array while maintaining the MinHeap property
	 * @param element	the new element to be inserted
	 */
	public void add(T element) {
		if(this.size==this.capacity-1) {//we cannot add an element to a full heap
			System.out.println("Coada plina");
			return;
		}
		this.size++;
		elements[size-1]=element;//adding the new element to the end/last leaf in heap
		int p=(size-1)/2;
		int i=size-1;
		while(p>=0&&elements[p].compareTo(elements[i])==1) {//while MinHeap property is violated
			T temp=elements[p];//we swap parent and child and continue upwards
			elements[p]=elements[i];
			elements[i]=temp;
			i=p;
			p=(i-1)/2;
		}
	}
	/**
	 * Restores the MinHeap property in a subtree rooted with the node at index in the array
	 * @param index	the index of the root of the subtree
	 */
	void heapify(int index) {
		int i=index;
		int l=2*i+1;
		int r=2*i+2;
		/* find smallest between root and his direct children */
		int smallest=i;
		if(l<this.size&&elements[l].compareTo(elements[i])==-1) {
			smallest=l;
		}
		if(r<this.size&&elements[r].compareTo(elements[smallest])==-1) {
			smallest=r;
		}
		/* if the smallest is one of the children,swap with parent and proceed downwards */
		if(smallest!=i) {
			T temp=elements[i];
			elements[i]=elements[smallest];
			elements[smallest]=temp;
			heapify(smallest);
		}
	}
	
	/**
	 * Extracts the minimum element from the structure and restores the MinHeap Property
	 * if required by employing heapify method
	 * @return the root of the MinHeap
	 */
	public T poll() {
		if(size==0) {//we cannot extract from an empty heap
			System.out.println("Coada vida");
			return null;
		}
		T result=elements[0];//we store the result,move the last leaf at first index and heapify
		elements[0]=elements[size-1];
		this.size--;
		heapify(0);
		return result;
	}
	
	/**
	 * Unlike poll method,this returns the minimum element without extracting it from the heap
	 * @return the root of the MinHeap
	 */
	public T peek() {
		if(size==0) {
			System.out.println("Coada vida");
			return null;
		}
		return elements[0];
	}
	/**
	 * Checks if the heap has no elements
	 * @return True if heap is empty,False otherwisr
	 */
	public boolean isEmpty() {
		return size==0;
	}
	
	
}
