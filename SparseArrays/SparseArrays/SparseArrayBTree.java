package es.uned.lsi.eped.pract2021_2022;

import es.uned.lsi.eped.DataStructures.BTreeIF;
import es.uned.lsi.eped.DataStructures.BTree;
import es.uned.lsi.eped.DataStructures.StackIF;
import es.uned.lsi.eped.DataStructures.Stack;
import es.uned.lsi.eped.DataStructures.IteratorIF;
import es.uned.lsi.eped.DataStructures.Queue;
import es.uned.lsi.eped.DataStructures.QueueIF;

public class SparseArrayBTree<E> extends BTree<E> implements SparseArrayIF<E> {

	protected BTreeIF<IndexedPair<E>> btree;
	
	public SparseArrayBTree() {
		super();
		this.btree = new BTree<IndexedPair<E>>();
	}
	
	private StackIF<Boolean> num2bin(int n) {
		Stack<Boolean> salida = new Stack<Boolean>();
		if ( n == 0 ) {
			salida.push(false);
		} else {
			while ( n != 0 ) {
				salida.push((n % 2) == 1);
				n = n / 2;
			}
		}
		return salida;
	}

	@Override
	public IteratorIF<E> iterator() {
		QueueIF<E> queue = new Queue<E>();
		BTreeIF<IndexedPair<E>> btreeLocal = this.btree;
		if ( this.size > 0 ) {
			QueueIF<BTreeIF<IndexedPair<E>>> auxQ = new Queue<BTreeIF<IndexedPair<E>>>();
			auxQ.enqueue(btreeLocal);
			while ( ! auxQ.isEmpty() ) {
				BTreeIF<IndexedPair<E>> cBT = auxQ.getFirst();
				if ( cBT.getLeftChild() != null ) {
					auxQ.enqueue(cBT.getLeftChild());
					if (cBT.getLeftChild().getRoot() != null) {
						queue.enqueue(cBT.getLeftChild().getRoot().getValue());
						}
					}
				if ( cBT.getRightChild() != null ) {
					auxQ.enqueue(cBT.getRightChild());
					if (cBT.getRightChild().getRoot() != null) {
						queue.enqueue(cBT.getRightChild().getRoot().getValue());
						}
					}
				auxQ.dequeue();
			}
		}
		return queue.iterator();
	}

	@Override
	public void set(int pos, E elem) {
//		long inicio = System.nanoTime();
//		for (int u = 1; u <= 1000000; u++) {
		StackIF<Boolean> salida = this.num2bin(pos);
		BTreeIF<IndexedPair<E>> btreeLocal = this.btree;
		while (!salida.isEmpty()) {
			if (salida.getTop()) {
				if (btreeLocal.getRightChild() == null) {
					BTreeIF<IndexedPair<E>> btreeNuevo = new BTree<IndexedPair<E>>();
					btreeLocal.setRightChild(btreeNuevo);
				}
				btreeLocal = btreeLocal.getRightChild();
			} else {
				if (btreeLocal.getLeftChild() == null) {
					BTreeIF<IndexedPair<E>> btreeNuevo = new BTree<IndexedPair<E>>();
					btreeLocal.setLeftChild(btreeNuevo);
				}
				btreeLocal = btreeLocal.getLeftChild();
			}
			salida.pop();
		}
		if (btreeLocal.getRoot() == null) {this.size++;}
		IndexedPair<E> iP = new IndexedPair<E>(pos, elem);
		btreeLocal.setRoot(iP);
//		}
//		long fin = System.nanoTime();
//		long tiempo = (fin - inicio) / 1000000; 
//		System.out.println("Set para la posición " + pos + " y con " +  (this.size - 1) + " elementos ha tardado " + tiempo + "ns");
	}

	@Override
	public E get(int pos) {
		E resultado = null;
//		long inicio = System.nanoTime();
//		for (int u = 1; u <= 1000000; u++) {
		StackIF<Boolean> salida = this.num2bin(pos);
		BTreeIF<IndexedPair<E>> btreeLocal = this.btree;
		while (!salida.isEmpty() && btreeLocal != null) {
			if (salida.getTop()) {
				btreeLocal = btreeLocal.getRightChild();
			} else {
				btreeLocal = btreeLocal.getLeftChild();
			}
			salida.pop();
		}
		if (btreeLocal != null) {
			if (btreeLocal.getRoot() != null) {
				resultado = btreeLocal.getRoot().getValue();
			}
		}
//		}
//		long fin = System.nanoTime();
//		long tiempo = (fin - inicio) / 1000000; 
//		System.out.println("Get para la posición " + pos + " y con " +  (this.size - 1) + " elementos ha tardado " + tiempo + "ns");
		return resultado;
	}

	@Override
	public void delete(int pos) {
		StackIF<Boolean> salida = this.num2bin(pos);
		BTreeIF<IndexedPair<E>> btreeLocal = this.btree;
		int arbolesVacios = 0;
		while (!salida.isEmpty() && btreeLocal != null) {
			if (salida.getTop()) {
				btreeLocal = btreeLocal.getRightChild();
			} else {
				btreeLocal = btreeLocal.getLeftChild();
			}
			if (btreeLocal == null) {
				break;
			}
			salida.pop();
			if (btreeLocal.getNumChildren() < 2 && btreeLocal.getRoot() == null && !salida.isEmpty()
					|| btreeLocal.getNumChildren() == 0 && salida.isEmpty()) {
				arbolesVacios++;
			} else {
				
				arbolesVacios = 0;
			}
		}

		if (btreeLocal != null) {
			if (btreeLocal.getRoot() != null) {
				this.size--;
				btreeLocal.setRoot(null);
				if (arbolesVacios > 0) {
					arbolesVacios--;
					while (arbolesVacios > 0) {
						arbolesVacios--;
						pos = pos / 2;
					}
					StackIF<Boolean> salidaEliminar = this.num2bin(pos);
					btreeLocal = this.btree;
					while (salidaEliminar.size() > 1) {
						if (salidaEliminar.getTop()) {
							btreeLocal = btreeLocal.getRightChild();
						} else {
							btreeLocal = btreeLocal.getLeftChild();
						}
					salidaEliminar.pop();
					}
					if (salidaEliminar.getTop()) {
						btreeLocal.setRightChild(null);
					} else {
						btreeLocal.setLeftChild(null);
					}
				}
			}
		}
	}
		

	@Override
	public IteratorIF<Integer> indexIterator() {
		QueueIF<Integer> queue = new Queue<Integer>();
		BTreeIF<IndexedPair<E>> btreeLocal = this.btree;
		if ( !this.isEmpty() ) {
			QueueIF<BTreeIF<IndexedPair<E>>> auxQ = new Queue<BTreeIF<IndexedPair<E>>>();
			auxQ.enqueue(btreeLocal);
			while ( ! auxQ.isEmpty() ) {
				BTreeIF<IndexedPair<E>> cBT = auxQ.getFirst();
				if ( cBT.getLeftChild() != null ) {
					auxQ.enqueue(cBT.getLeftChild());
					if (cBT.getLeftChild().getRoot() != null) {
						queue.enqueue(cBT.getLeftChild().getRoot().getIndex());
						}
					}
				if ( cBT.getRightChild() != null ) {
					auxQ.enqueue(cBT.getRightChild());
					if (cBT.getRightChild().getRoot() != null) {
						queue.enqueue(cBT.getRightChild().getRoot().getIndex());
						}
					}
				auxQ.dequeue();
			}
		}
		return queue.iterator();
	}
	
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	public int size() {
		return this.size;
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
		this.btree.clear();
		this.size = 0;
	}
}
