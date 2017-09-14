
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;




public class GrafoLista<V extends Comparable<V>> implements IGrafo<V> {

	private boolean dirigido;

	private int size;

	
	
	//para floyd-warshall
	private double[][] matrizPesos;
	private ArrayList<Vertice<V>>[][] matrizCaminos;

	
	//para dijkstra
	private ArrayList<Vertice<V>>[] c;
	private double[] l;
	
	private static int MAX = Integer.MAX_VALUE;

	private TreeMap<Vertice<V>, TreeSet<Arista<V>>> treeMap;

	public GrafoLista() {
		size = 0;
		dirigido = false;
		treeMap = new TreeMap<Vertice<V>, TreeSet<Arista<V>>>();
	}

	public GrafoLista(boolean dirigido) {
		treeMap = new TreeMap<Vertice<V>, TreeSet<Arista<V>>>();
		size = 0;
		this.dirigido = dirigido;
	}

	public void Dijkstra(V valor) {
		Dijkstra(buscar(valor));
	}

	class nodo implements Comparable<nodo> {
		private double distancia;
		private Vertice<V> vertice;

		public nodo(Vertice<V> v, double d) {
			distancia = d;
			vertice = v;
		}

		public double getDistancia() {
			return distancia;
		}

		public void setDistancia(double distancia) {
			this.distancia = distancia;
		}

		public Vertice<V> getVertice() {
			return vertice;
		}

		public void setVertice(Vertice<V> vertice) {
			this.vertice = vertice;
		}

		@Override
		public int compareTo(GrafoLista<V>.nodo o) {
			if (distancia < o.distancia) {
				return -1;
			} else if (distancia > o.distancia) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public void Dijkstra(Vertice<V> a) {

		l = new double[size];
		for (int i = 0; i < l.length; i++) {
			l[i] = MAX;
		}
		l[a.getPos()] = 0;

		c = new ArrayList[size];
		for (int i = 0; i < c.length; i++) {
			c[i] = new ArrayList<Vertice<V>>();
		}
		c[a.getPos()].add(a);

		PriorityQueue<nodo> pq = new PriorityQueue<nodo>();

		pq.add(new nodo(a, 0));
		while (!pq.isEmpty()) {
			nodo n;
			while (pq.size() > 0 && !pq.peek().getVertice().getColor().equals(Vertice.WHITE)) {
				pq.poll();
			}
			if (!pq.isEmpty()) {
				n = pq.poll();
				Vertice<V> u = n.getVertice();
				u.setColor(Vertice.GRAY);
				int tam = treeMap.get(u).size();
				Iterator<Arista<V>> it = treeMap.get(u).iterator();

				for (int i = 0; i < tam; i++) {

					Vertice<V> v = it.next().getVertex2();

					if (v.getColor().equals(Vertice.WHITE)) {
						double w = consultarCosto(u, v);
						int posActual = u.getPos();
						int posAdyacente = v.getPos();
						if (l[posActual] + w < l[posAdyacente]) {

							l[posAdyacente] = l[posActual] + w;
							c[posAdyacente] = (ArrayList<Vertice<V>>) c[posActual].clone();
							c[posAdyacente].add(v);
							pq.add(new nodo(v, l[posAdyacente]));

						}
					}
				}
			}
		}
		limpiarVertices();
	}
	
	public void imprimirCaminoDijkstra(){
		
		for (int i = 0; i < c.length; i++) {
			 for (int j = 0; j < c[i].size(); j++) {
			 System.out.print(c[i].get(j).getValue()+"|");
			 }
			 System.out.println();
			 }
		
		
	}
	
	public void imprimirPesoDijkstra(){
		 for (int i = 0; i < l.length; i++) {
			 System.out.print(l[i]+"|");
			 }
		 System.out.println();
	}
	
	
//para floyd-warshall
	public double[][] generarMatrizCosto() {

		double[][] retorno = new double[size][size];

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {
				if (i == j) {
					retorno[i][j] = 0;
				} else

					retorno[i][j] = MAX;
			}
		}

		Iterator<Vertice<V>> it = treeMap.keySet().iterator();

		while (it.hasNext()) {
			Vertice<V> noTieneArista = it.next();

			Iterator<Arista<V>> itArista = treeMap.get(noTieneArista).iterator();

			while (itArista.hasNext()) {
				Arista<V> aristaTemp = itArista.next();

				retorno[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex2().getPos()] = aristaTemp.getCost();

			}

		}

		return retorno;
	}

	//para floyd-warshall
	public void generarMatrizCaminos() {
		matrizCaminos = new ArrayList[size][size];

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				matrizCaminos[i][j] = new ArrayList<Vertice<V>>();
						
			}
		}

		Iterator<Vertice<V>> it = treeMap.keySet().iterator();

		while (it.hasNext()) {
			Vertice<V> noTieneArista = it.next();

			if (treeMap.get(noTieneArista).isEmpty()) {
				ArrayList<Vertice<V>> temp = new ArrayList<>();
				temp.add(noTieneArista);

				matrizCaminos[noTieneArista.getPos()][noTieneArista.getPos()] = temp;

			} else {
				
				Iterator<Arista<V>> itArista = treeMap.get(noTieneArista).iterator();

				
				while (itArista.hasNext()) {
					
//					System.out.println("1 vez");
					Arista<V> aristaTemp = itArista.next();
					matrizCaminos[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex2().getPos()]
							.add(aristaTemp.getVertex1());
					
					matrizCaminos[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex2().getPos()]
							.add(aristaTemp.getVertex2());
					
					

					if (matrizCaminos[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex1().getPos()].isEmpty()) {
						ArrayList<Vertice<V>> temp = new ArrayList<>();
						temp.add(aristaTemp.getVertex1());

						matrizCaminos[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex1().getPos()] = temp;
					}
					if (matrizCaminos[aristaTemp.getVertex2().getPos()][aristaTemp.getVertex2().getPos()].isEmpty()) {
						ArrayList<Vertice<V>> temp = new ArrayList<>();
						temp.add(aristaTemp.getVertex2());

						matrizCaminos[aristaTemp.getVertex2().getPos()][aristaTemp.getVertex2().getPos()] = temp;

				}
			}
		}
		}
	}

	public void FloydWarshall() {

		matrizPesos = generarMatrizCosto();

		generarMatrizCaminos();

		for (int k = 0; k < size; k++) {

			for (int i = 0; i < size; i++) {

				for (int j = 0; j < size; j++) {

					// W=matrizPesos[k][j]
					if (matrizPesos[i][k] + matrizPesos[k][j] < matrizPesos[i][j]) {

						matrizPesos[i][j] = matrizPesos[i][k] + matrizPesos[k][j];


						matrizCaminos[i][j]=new ArrayList();
						matrizCaminos[i][j].add(matrizCaminos[i][i].get(0));
						matrizCaminos[j][i]=new ArrayList();

							for (int l = 0; l < matrizCaminos[k][j].size(); l++) {
							
								matrizCaminos[i][j].add(matrizCaminos[k][j].get(l));
							
							
						}
							for (int l = matrizCaminos[k][j].size()-1; l >=0 ; l--) {
						
								matrizCaminos[j][i].add(matrizCaminos[k][j].get(l));
							
						}
							matrizCaminos[j][i].add(matrizCaminos[i][i].get(0));

					}
				}
			}

		}

	}
	
	public void imprimirMatrizPesosFloydWarshall(){
		
		for (int i = 0; i < size(); i++) {

			for (int j = 0; j < size(); j++) {

				System.out.print(getMatrizPesos()[i][j] + "|");
			}
			System.out.println();
		}
		
	}
	public void imprimirMatrizCaminosFloydWarshall(){
		
		for (int i = 0; i < size(); i++) {

			for (int j = 0; j < size(); j++) {

				Iterator<Vertice<V>> it = matrizCaminos[i][j].iterator();
				if(!it.hasNext())System.out.print("0 0 ");
				while (it.hasNext()) {
					System.out.print(it.next().getValue()+" ");
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
	public double[][] getMatrizPesos() {
		return matrizPesos;
	}

	public void setMatrizPesos(double[][] matrizPesos) {
		this.matrizPesos = matrizPesos;
	}

	

	public ArrayList<Vertice<V>>[][] getMatrizCaminos() {
		return matrizCaminos;
	}

	public void setMatrizCaminos(ArrayList<Vertice<V>>[][] matrizCaminos) {
		this.matrizCaminos = matrizCaminos;
	}

	private double consultarCosto(Vertice<V> vertice1, Vertice<V> vertice2) {
		Iterator<Arista<V>> it = treeMap.get(vertice1).iterator();

		while (it.hasNext()) {
			Arista<V> temp = it.next();
			if (temp.getVertex2() == vertice2) {
				return temp.getCost();
			}
		}
		return 0;
	}

	public double consultarCosto(V vertice1, V vertice2) {
		Vertice<V> keyV1 = buscar(vertice1);
		Vertice<V> keyV2 = buscar(vertice2);

		Iterator<Arista<V>> it = treeMap.get(keyV1).iterator();

		while (it.hasNext()) {
			Arista<V> temp = it.next();
			if (temp.getVertex2() == keyV2) {
				return temp.getCost();
			}
		}
		return 0;
	}

	@Override
	public boolean agregar(V v) {
		return agregar(new Vertice<V>(v));
	}

	private boolean agregar(Vertice<V> v) {
		if (!treeMap.containsKey(v)) {
			v.setPos(size);
			treeMap.put(v, new TreeSet<Arista<V>>());
			size++;
			return true;
		} else
			return false;
	}

	@Override
	public boolean conectar(V v1, V v2) {

		Vertice<V> inicio = buscar(v1);
		Vertice<V> fin = buscar(v2);
		return conectar(inicio, fin);
	}

	@Override
	public boolean conectar(V v1, V v2, double costo) {
		Vertice<V> inicio = buscar(v1);
		Vertice<V> fin = buscar(v2);
		return conectar(inicio, fin, costo);
	}

	public Vertice<V> buscar(V valor) {
		Vertice<V> vert = null;
		Set<Vertice<V>> keys = treeMap.keySet();
		for (Iterator<Vertice<V>> i = keys.iterator(); i.hasNext();) {
			vert = i.next();
			if (vert.getValue().compareTo(valor) == 0) {
				return vert;
			}
		}
		return vert;
	}

	private boolean conectar(Vertice<V> inicio, Vertice<V> fin) {
		if (!dirigido) {
			if (treeMap.containsKey(inicio) && treeMap.containsKey(fin)) {
				Arista<V> aristTemp = new Arista<V>(inicio, fin);
				treeMap.get(inicio).add(aristTemp);
				Arista<V> aristTemp2 = new Arista<V>(fin, inicio);
				treeMap.get(fin).add(aristTemp2);
				return true;
			} else {
				return false;
			}
		} else {
			if (treeMap.containsKey(inicio) && treeMap.containsKey(fin)) {
				Arista<V> aristTemp = new Arista<V>(inicio, fin);
				treeMap.get(inicio).add(aristTemp);
				return true;
			} else
				return false;
		}
	}

	private boolean conectar(Vertice<V> inicio, Vertice<V> fin, double costo) {
		if (!dirigido) {
			if (treeMap.containsKey(inicio) && treeMap.containsKey(fin)) {
				Arista<V> aristTemp = new Arista<V>(inicio, fin, costo);
				treeMap.get(inicio).add(aristTemp);
				Arista<V> aristTemp2 = new Arista<V>(fin, inicio, costo);
				treeMap.get(fin).add(aristTemp2);
			} else {
				return false;
			}
		} else {
			if (treeMap.containsKey(inicio) && treeMap.containsKey(fin)) {
				Arista<V> aristTemp = new Arista<V>(inicio, fin, costo);
				treeMap.get(inicio).add(aristTemp);
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public ArrayList<V> recorridoBFS(V source) {
		return recorridoBFS(buscar(source));
	}

	private ArrayList<V> recorridoBFS(Vertice<V> source) {
		ArrayList<V> array = new ArrayList<V>(size);
		Queue<Vertice<V>> queue = new ArrayDeque<>();
		queue.add(source);

		while (!queue.isEmpty()) {
			Vertice<V> que = queue.poll();
			if (!que.getColor().equals(Vertice.BLACK)) {
				que.setColor(Vertice.BLACK);
				array.add(que.getValue());

				Iterator<Arista<V>> it = treeMap.get(que).iterator();

				while (it.hasNext()) {
					Vertice<V> vertice = it.next().getVertex2();
					if (!vertice.getColor().equals(Vertice.BLACK)) {
						queue.add(vertice);
						vertice.setColor(Vertice.GRAY);
					}
				}
			}
		}

		limpiarVertices();
		return array;
	}

	@Override
	public ArrayList<V> recorridoDFS(V source) {
		return recorridoDFS(buscar(source));
	}

	private ArrayList<V> recorridoDFS(Vertice<V> source) {
		ArrayList<V> array = new ArrayList<V>(size);
		Stack<Vertice<V>> stack = new Stack<>();
		stack.add(source);

		while (!stack.isEmpty()) {
			Vertice<V> sta = stack.pop();
			if (!sta.getColor().equals(Vertice.BLACK)) {
				sta.setColor(Vertice.BLACK);
				array.add(sta.getValue());

				Iterator<Arista<V>> it = treeMap.get(sta).iterator();
				Stack<Vertice<V>> temp = new Stack<>();

				while (it.hasNext()) {
					Vertice<V> vertice = it.next().getVertex2();
					if (!vertice.getColor().equals(Vertice.BLACK)) {
						temp.add(vertice);
						vertice.setColor(Vertice.GRAY);
					}
				}
				while (!temp.isEmpty()) {
					stack.add(temp.pop());
				}
			}
		}

		limpiarVertices();
		return array;
	}

	public TreeMap<Vertice<V>, TreeSet<Arista<V>>> getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(TreeMap<Vertice<V>, TreeSet<Arista<V>>> treeMap) {
		this.treeMap = treeMap;
	}

	public double[] getL() {
		return l;
	}

	public void setL(double[] l) {
		this.l = l;
	}

	public ArrayList<Vertice<V>>[] getC() {
		return c;
	}

	public void setC(ArrayList<Vertice<V>>[] c) {
		this.c = c;
	}

	@Override
	public void limpiarVertices() {
		Set<Vertice<V>> keys = treeMap.keySet();
		for (Iterator<Vertice<V>> i = keys.iterator(); i.hasNext();) {
			i.next().setColor(Vertice.WHITE);
		}
	}

	@Override
	public int size() {
		return size;
	}
}


 class Arista<T extends Comparable<T>> implements Comparable<Arista<T>> {

	private Vertice<T> vertex1;

	private Vertice<T> vertex2;

	private double cost;

	public Arista(Vertice<T> vertex1, Vertice<T> vertex2, double cost) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.cost = cost;
	}

	public Arista(Vertice<T> vertex1, Vertice<T> vertex2) {
		cost = 0;
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}

	@Override
	public int compareTo(Arista<T> o) {
		if(cost>o.cost){
			return 1;
		}else if(cost<o.cost){
			return -1;
		}else{
			return 0;
		}
	}

	public Vertice<T> getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vertice<T> vertex1) {
		this.vertex1 = vertex1;
	}

	public Vertice<T> getVertex2() {
		return vertex2;
	}

	public void setVertex2(Vertice<T> vertex2) {
		this.vertex2 = vertex2;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}


 class Vertice<T extends Comparable<T>> implements Comparable<Vertice<T>> {

	public final static String WHITE = "Blanco";
	public final static String GRAY = "Gris";
	public final static String BLACK = "Negro";	
	
	private T value;
	
	private int pos;
	
	private String color;
	
	public Vertice(T val) {
		value = val;
		color = WHITE;
	}	
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}	

	@Override
	public int compareTo(Vertice<T> o) {
		return value.compareTo(o.getValue());
	}
}


 interface IGrafo<V extends Comparable<V>> {

	public boolean agregar(V vertice);

	public boolean conectar(V vertice1, V vertice2);

	public boolean conectar(V vertice1, V vertice2, double costo);

	public ArrayList<V> recorridoDFS(V source);

	public ArrayList<V> recorridoBFS(V source);

	public void limpiarVertices();

	public int size();
}