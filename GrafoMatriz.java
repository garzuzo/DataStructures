
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;



public class GrafoMatriz<V extends Comparable<V>> implements IGrafo<V> {
	private TreeSet<Arista<V>>[][] matriz;
	private ArrayList<Vertice<V>> agregados;
	private boolean dirigido;
	private int size;
	private int cont;

	// dijkstra
	private double[] l;
	private ArrayList<Vertice<V>>[] c;

	// floyd-warshall
	private double[][] matrizPesos;
	private ArrayList<Vertice<V>>[][] matrizCaminos;

	private static int MAX = Integer.MAX_VALUE;

	public ArrayList<Vertice<V>> getAgregados() {
		return agregados;
	}

	public void setAgregados(ArrayList<Vertice<V>> agregados) {
		this.agregados = agregados;
	}

	public TreeSet<Arista<V>>[][] getMatriz() {
		return matriz;
	}

	public GrafoMatriz(int tamano) {
		matriz = new TreeSet[tamano][tamano];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {

				matriz[i][j] = new TreeSet<Arista<V>>();

			}
		}
		cont = 0;
		size = tamano;
		agregados = new ArrayList<Vertice<V>>();
		dirigido = false;
	}

	public GrafoMatriz(int tamano, boolean dirigido) {
		matriz = new TreeSet[tamano][tamano];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {

				matriz[i][j] = new TreeSet<Arista<V>>();

			}
		}
		cont = 0;
		size = tamano;
		agregados = new ArrayList<Vertice<V>>();
		this.dirigido = dirigido;
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
		public int compareTo(GrafoMatriz<V>.nodo o) {
			if (distancia < o.distancia) {
				return -1;
			} else if (distancia > o.distancia) {
				return 1;
			} else {
				return 0;
			}
		}

	}

	public void Dijkstra(V val) {
		Dijkstra(buscar(val));
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

				ArrayList<Vertice<V>> adyacentes = new ArrayList<Vertice<V>>();

				for (int i = 0; i < c.length; i++) {

					if (!matriz[u.getPos()][i].isEmpty()) {

						adyacentes.add(matriz[u.getPos()][i].first().getVertex2());
					}
				}

				int tam = adyacentes.size();

				for (int i = 0; i < tam; i++) {

					Vertice<V> v = adyacentes.get(i);

					if (v.getColor().equals(Vertice.WHITE)) {
						int posActual = u.getPos();
						int posAdyacente = v.getPos();

						double w = matriz[posActual][posAdyacente].first().getCost();

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

	public void imprimirCaminoDijkstra() {

		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i].size(); j++) {
				System.out.print(c[i].get(j).getValue() + "|");
			}
			System.out.println();
		}

	}

	public void imprimirPesoDijkstra() {
		for (int i = 0; i < l.length; i++) {
			System.out.print(l[i] + "|");
		}
		System.out.println();
	}

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

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				if (!matriz[i][j].isEmpty()) {
					Arista<V> aristaTemp = matriz[i][j].first();
					retorno[aristaTemp.getVertex1().getPos()][aristaTemp.getVertex2().getPos()] = aristaTemp.getCost();
				}

			}
		}

		return retorno;
	}

	public void generarMatrizCaminos() {
		matrizCaminos = new ArrayList[size][size];

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				matrizCaminos[i][j] = new ArrayList<Vertice<V>>();

			}
		}

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

				if (matriz[i][j].isEmpty()) {
					ArrayList<Vertice<V>> temp = new ArrayList<>();
					temp.add(agregados.get(i));
					matrizCaminos[agregados.get(i).getPos()][agregados.get(i).getPos()] = temp;

				} else {

					Arista<V> aristaTemp = matriz[i][j].first();
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

						matrizCaminos[i][j] = new ArrayList();
						matrizCaminos[i][j].add(matrizCaminos[i][i].get(0));
						matrizCaminos[j][i] = new ArrayList();

						for (int l = 0; l < matrizCaminos[k][j].size(); l++) {

							matrizCaminos[i][j].add(matrizCaminos[k][j].get(l));

						}
						for (int l = matrizCaminos[k][j].size() - 1; l >= 0; l--) {

							matrizCaminos[j][i].add(matrizCaminos[k][j].get(l));

						}
						matrizCaminos[j][i].add(matrizCaminos[i][i].get(0));

					}
				}
			}

		}

	}

	public void imprimirMatrizPesosFloydWarshall() {

		for (int i = 0; i < size(); i++) {

			for (int j = 0; j < size(); j++) {
				if (getMatrizPesos()[i][j] == MAX) {
					System.out.print("-1 ");
				} else{
					System.out.print((int) getMatrizPesos()[i][j]+" " );
				}
//				if(j+1!=size())
//					System.out.print(" ");
			}
			System.out.println();
		}

	}

	public void imprimirMatrizCaminosFloydWarshall() {

		for (int i = 0; i < size(); i++) {

			for (int j = 0; j < size(); j++) {

				Iterator<Vertice<V>> it = matrizCaminos[i][j].iterator();
				if (!it.hasNext())
					System.out.print("0 0 ");
				while (it.hasNext()) {
					System.out.print(it.next().getValue() + " ");
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

	@Override
	public boolean agregar(V vertice) {

		return agregar(new Vertice<V>(vertice));
	}

	private boolean agregar(Vertice<V> vertice) {

		agregados.add(vertice);
		vertice.setPos(cont);
		cont++;
		return true;
	}

	@Override
	public boolean conectar(V vertice1, V vertice2) {

		return conectar(buscar(vertice1), buscar(vertice2));
	}

	private boolean conectar(Vertice<V> vertice1, Vertice<V> vertice2) {

		Arista<V> nueva = new Arista<V>(vertice1, vertice2);
		if (vertice1 != null && vertice2 != null) {
			if (dirigido) {
				matriz[vertice1.getPos()][vertice2.getPos()].add(nueva);
				return true;
			} else {
				Arista<V> nueva1 = new Arista<V>(vertice2, vertice1);
				matriz[vertice1.getPos()][vertice2.getPos()].add(nueva);
				matriz[vertice2.getPos()][vertice1.getPos()].add(nueva1);
				return true;
			}
		} else
			return false;
	}

	@Override
	public boolean conectar(V vertice1, V vertice2, double costo) {

		return conectar(buscar(vertice1), buscar(vertice2), costo);
	}

	public Vertice<V> buscar(V val) {

		for (int i = 0; i < agregados.size(); i++) {

			if (agregados.get(i).getValue().compareTo(val) == 0) {
				return agregados.get(i);
			}
		}
		return null;
	}

	private boolean conectar(Vertice<V> vertice1, Vertice<V> vertice2, double costo) {

		Arista<V> nueva = new Arista<V>(vertice1, vertice2, costo);
		if (vertice1 != null && vertice2 != null) {
			if (dirigido) {
				matriz[vertice1.getPos()][vertice2.getPos()].add(nueva);
				return true;
			} else {
				Arista<V> nueva1 = new Arista<V>(vertice2, vertice1, costo);
				matriz[vertice1.getPos()][vertice2.getPos()].add(nueva);
				matriz[vertice2.getPos()][vertice1.getPos()].add(nueva1);
				return true;
			}
		} else
			return false;
	}

	@Override
	public ArrayList<V> recorridoDFS(V source) {

		return recorridoDFS(buscar(source));
	}

	private ArrayList<V> recorridoDFS(Vertice<V> source) {
		ArrayList<V> retorno = new ArrayList<V>();
		Stack<Vertice<V>> pila = new Stack<Vertice<V>>();
		pila.push(source);

		while (!pila.isEmpty()) {

			Vertice<V> temp = pila.pop();

			if (!temp.getColor().equals(Vertice.BLACK)) {

				temp.setColor(Vertice.BLACK);
				retorno.add(temp.getValue());

				for (int i = matriz[1].length - 1; i >= 0; i--) {
					if (matriz[temp.getPos()][i].size() != 0) {
						Stack<Vertice<V>> pilaTemp = new Stack<Vertice<V>>();
						Iterator<Arista<V>> it = matriz[temp.getPos()][i].iterator();

						while (it.hasNext()) {
							Arista<V> itNext = it.next();
							if (!itNext.getVertex2().getColor().equals(Vertice.BLACK)) {
								itNext.getVertex2().setColor(Vertice.GRAY);
								pilaTemp.add(itNext.getVertex2());
							}
						}

						while (!pilaTemp.isEmpty())
							pila.add(pilaTemp.pop());

					}

				}
			}

		}
		limpiarVertices();
		return retorno;
	}

	public void imprimir() {

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				System.out.print(matriz[i][j].size() + " ");
			}
			System.out.println();
		}

	}

	@Override
	public ArrayList<V> recorridoBFS(V source) {

		return recorridoBFS(buscar(source));
	}

	public ArrayList<V> recorridoBFS(Vertice<V> source) {
		ArrayList<V> array = new ArrayList<V>();
		Queue<Vertice<V>> queue = new ArrayDeque<Vertice<V>>();
		queue.add(source);
		while (!queue.isEmpty()) {
			Vertice<V> que = queue.poll();
			if (!que.getColor().equals(Vertice.BLACK)) {
				que.setColor(Vertice.BLACK);
				array.add(que.getValue());

				for (int i = 0; i < matriz[1].length; i++) {
					if (matriz[que.getPos()][i].size() != 0) {
						Iterator<Arista<V>> it = matriz[que.getPos()][i].iterator();

						while (it.hasNext()) {
							Arista<V> itNext = it.next();
							if (!itNext.getVertex2().getColor().equals(Vertice.BLACK)) {
								itNext.getVertex2().setColor(Vertice.GRAY);
								queue.add(itNext.getVertex2());
							}
						}
					}
				}
			}
		}
		limpiarVertices();
		return array;
	}

	@Override
	public void limpiarVertices() {

		for (int i = 0; i < agregados.size(); i++) {
			agregados.get(i).setColor(Vertice.WHITE);
		}

	}

	@Override
	public int size() {

		return size;
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
		pos = 0;
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

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public int compareTo(Vertice<T> o) {
		return value.compareTo(o.getValue());
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
//	cost
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



 interface IGrafo<V extends Comparable<V>> {

	public boolean agregar(V vertice);

	public boolean conectar(V vertice1, V vertice2);

	public boolean conectar(V vertice1, V vertice2, double costo);

	public ArrayList<V> recorridoDFS(V source);

	public ArrayList<V> recorridoBFS(V source);

	public void limpiarVertices();

	public int size();
}
