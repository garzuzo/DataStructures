import static java.lang.Math.min;
import java.io.*;
import java.util.*;

public class Census {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader((System.in)));

		int n = Integer.parseInt(in.readLine());

		SegTree[] arr = new SegTree[n];

		for (int i = 0; i < n; i++) {
			SegTree stAct = new SegTree(0, n - 1);
			String rowAct[] = in.readLine().split(" ");

			for (int j = 0; j < n; j++) {

				stAct.set(j, Integer.parseInt(rowAct[j]));

			}
			arr[i] = stAct;

		}
		int q = Integer.parseInt(in.readLine());

		for (int i = 0; i < q; i++) {

			String[] act = in.readLine().split(" ");

			String cAct = act[0];

			if (cAct.equals("q")) {

				int x1 = Integer.parseInt(act[1]) - 1;
				int y1 = Integer.parseInt(act[2]) - 1;
				int x2 = Integer.parseInt(act[3]) - 1;
				int y2 = Integer.parseInt(act[4]) - 1;
				int minVal = Integer.MAX_VALUE;
				int maxVal = Integer.MIN_VALUE;

				for (int j = x1; j <= x2; j++) {
					SegTree st1 = arr[j];
					minVal = Math.min(minVal, st1.getMin(y1, y2));
					maxVal = Math.max(st1.getMax(y1, y2), maxVal);

				}
				System.out.println(maxVal+" "+minVal);

			} else {
				int x = Integer.parseInt(act[1]) - 1;
				int y = Integer.parseInt(act[2]) - 1;
				int v = Integer.parseInt(act[3]);

				arr[x].set(y, v);
			}

		}

	}

	static class SegTree {

		int minVal;
		int maxVal;
		int start;
		int end;
		SegTree left;
		SegTree right;

		SegTree(int start, int end) {
			this.start = start;
			this.end = end;
			this.minVal = Integer.MAX_VALUE;
			this.maxVal = Integer.MIN_VALUE;

			if (start == end) {
				left = null;
				right = null;
			} else {
				int mid = (start + end) / 2;
				left = new SegTree(start, mid);
				right = new SegTree(mid + 1, end);
			}
		}

		public void set(int pos, int val) {
			if (this.start == this.end && this.start == pos) {
				this.minVal = val;
				this.maxVal=val;
				return;
			}

			int mid = (start + end) / 2;

			if (pos <= mid)
				left.set(pos, val);
			else
				right.set(pos, val);

			this.minVal = min(left.minVal, right.minVal);
			this.maxVal=Math.max(left.maxVal, right.maxVal);
		}

		public int getMin(int low, int high) {

			// [low,high] = [start,end]
			if(start == low && end == high)
				return minVal;

			int mid = (start + end) / 2;

			// [low,high] totalmente incluido en [start,mid]
			if(high <= mid)
				return left.getMin(low, high);

			// [low,high] totalmente incluido en [mid+1,end]
			if(low > mid)
				return right.getMin(low, high);

			// debemos dividir el intervalo en [low,mid] y [mid+1,high]
			int leftMin = left.getMin(low, mid);
			int rightMin = right.getMin(mid+1, high);
			return Math.min(leftMin, rightMin);

}
		public int getMax(int low, int high) {

			// [low,high] = [start,end]
			if(start == low && end == high)
				return maxVal;

			int mid = (start + end) / 2;

			// [low,high] totalmente incluido en [start,mid]
			if(high <= mid)
				return left.getMax(low, high);

			// [low,high] totalmente incluido en [mid+1,end]
			if(low > mid)
				return right.getMax(low, high);

			// debemos dividir el intervalo en [low,mid] y [mid+1,high]
			int leftMax = left.getMax(low, mid);
			int rightMax= right.getMax(mid+1, high);
			return Math.max(leftMax, rightMax);

}

	}

}
