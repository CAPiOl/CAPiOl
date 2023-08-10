package es.uned.lsi.eped.pract2021_2022;

import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.List;
import es.uned.lsi.eped.DataStructures.Queue;
import es.uned.lsi.eped.DataStructures.QueueIF;
import es.uned.lsi.eped.DataStructures.Sequence;

public class SparseArraySequence<E>  extends Sequence<E>  implements SparseArrayIF<E> {

	protected List<IndexedPair<E>> list;
	
	public SparseArraySequence() {
		super();
		list = new List<IndexedPair<E>>();
	}
	
	public SparseArraySequence(SparseArraySequence<E> s) {
		super(s);
	}

	@Override
	public void delete(int pos) {
		int i = 1;
		IteratorIF<Integer> iter = this.indexIterator();
		while (iter.hasNext()) {
			int indice = iter.getNext();
			if (indice == pos) {
				list.remove(i);
				this.size--;
			} else if (indice > pos) {break;}
			i++;
		}
	}

	@Override
	public IteratorIF<Integer> indexIterator() {
		QueueIF<Integer> queue = new Queue<Integer>();
		IteratorIF<IndexedPair<E>> iterLista = list.iterator();
		while (iterLista.hasNext()) {
			queue.enqueue(iterLista.getNext().getIndex());
		}
		return queue.iterator();
	}

	@Override
	public void set(int pos, E elem) {
		IndexedPair<E> iP = new IndexedPair<E>(pos, elem);
//		long total = 0;
//		for (int u = 1; u <= 100000; u++) {
		int i = 1;
		IteratorIF<Integer> iter = this.indexIterator();
//		long inicio = System.nanoTime();
		while (iter.hasNext()) {
			int indice = iter.getNext();
			if (indice == pos) {
				list.set(i, iP);
				break;
			} else if (indice > pos) {
				list.insert(i, iP);
				this.size++;
				break;
			}
			i++;
		}
		if (i > this.size) {
			list.insert(i, iP);
			this.size++;
		}
//		long fin = System.nanoTime();
//		total = total + fin - inicio;
//		}
//		total = (total) / 100000; 
//		System.out.println("Set para la posición " + pos + " y con " +  (this.size - 1) + " elementos ha tardado " + total + "ns");
		
	}

	@Override
	public E get(int pos) {
		E resultado = null;
//		long total = 0;
//		for (int u = 1; u <= 100000; u++) {
		int i = 1;
		IteratorIF<Integer> iter = this.indexIterator();
//		long inicio = System.nanoTime();
		while (iter.hasNext()) {
			int indice = iter.getNext();
			if (indice == pos) {
				resultado = list.get(i).getValue();
				break;
			} else if (indice > pos) {break;}
			i++;
		}
//		long fin = System.nanoTime();
//		total = total + fin - inicio;
//		}
//		total = (total) / 100000; 
//		System.out.println("Get para la posición " + pos + " y con " +  (this.size) + " elementos ha tardado " + total + "ns");
		return resultado;
	}

	@Override
	public IteratorIF<E> iterator() {
		QueueIF<E> queue = new Queue<E>();
		IteratorIF<IndexedPair<E>> iterLista = list.iterator();
		while (iterLista.hasNext()) {
			queue.enqueue(iterLista.getNext().getValue());
		}
		return queue.iterator();
	}
	
	public boolean contains(E e) {
		IteratorIF<E> iter = this.iterator();
		while (iter.hasNext()) {
			if (iter.getNext().equals(e)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		this.list.clear();
		this.size = 0;
	}
	
}
