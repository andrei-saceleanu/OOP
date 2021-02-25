package my_pair;

/**
 * Generic Tuple of three elements
 * used in current application to model a proposal
 * @author Saceleanu Andrei
 *
 */
public class MyTuple <U,V,T>{
	private U fst;
	private V snd;
	private T trd;
	public MyTuple(U fst, V snd,T trd) {
		this.fst = fst;
		this.snd = snd;
		this.trd = trd;
	}
	public U getFst() {
		return fst;
	}
	public void setFst(U fst) {
		this.fst = fst;
	}
	public V getSnd() {
		return snd;
	}
	public void setSnd(V snd) {
		this.snd = snd;
	}
	public T getTrd() {
		return trd;
	}
	public void setTrd(T trd) {
		this.trd = trd;
	}
	

}
