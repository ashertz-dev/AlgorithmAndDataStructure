package tree;

/**
 * @author Hypnos Tsang
 * @date 2020/7/4
 */
public class SegmentTree<E> {
    private final E[] data;
    private final E[] tree;
    private final Merge<E> merge;

    @SuppressWarnings("unchecked")
    public SegmentTree(E[] arr,Merge<E> merge){
        this.merge = merge;
        data = (E[]) new Object[arr.length];
        tree = (E[]) new Object[arr.length * 4];
        System.arraycopy(arr, 0, data, 0, arr.length);
        buildSegmentTree(0, 0, data.length - 1);
    }

    /**
     * 在treeIndex的位置创建表示区间[l,r]的线段树
     * @param treeIndex 根节点索引
     * @param l 开始索引
     * @param r 结束索引
     */
    private void buildSegmentTree(int treeIndex, int l, int r){
        if (l == r){
            tree[treeIndex] = data[l];
            return;
        }
        int leftChildIndex = leftChild(treeIndex);
        int rightChildIndex = rightChild(treeIndex);
        int mid = l + (r - l) / 2;
        buildSegmentTree(leftChildIndex, l, mid);
        buildSegmentTree(rightChildIndex, mid + 1, r);
        tree[treeIndex] = merge.merge(tree[leftChildIndex], tree[rightChildIndex]);
    }

    public int getSize(){
        return data.length;
    }

    public E get(int index){
        if (index < 0 || index >= data.length){
            throw new IllegalArgumentException("Index is illegal");
        }
        return data[index];
    }

    private int leftChild(int index){
        return 2 * index + 1;
    }

    private int rightChild(int index){
        return 2 * index + 2;
    }

    public E query(int queryL, int queryR){
        if (queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length){
            throw new IllegalArgumentException("Index is illegal");
        }
        return query(0, 0, data.length - 1, queryL, queryR);
    }

    private E query(int treeIndex, int l, int r, int queryL, int queryR){
        if (l == queryL && r == queryR){
            return tree[treeIndex];
        }
        int mid = l + (r - l) / 2;
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);
        if (queryL >= mid + 1){
            return query(rightTreeIndex, mid + 1, r, queryL, queryR);
        }
        else if (queryR <= mid){
            return query(leftTreeIndex, l, mid, queryL,queryR);
        }
        E leftResult = query(leftTreeIndex, l, mid, queryL, mid);
        E rightResult = query(rightTreeIndex, mid + 1, r, mid + 1, queryR);
        return merge.merge(leftResult, rightResult);
    }

    public void set(int index, E e){
        if (index < 0 || index >= data.length){
            throw new IllegalArgumentException("Index is illegal");
        }
        data[index] = e;
        set(0, 0, data.length-1, index, e);
    }

    private void set(int treeIndex, int l, int r, int index, E e){
        if (l == r){
            tree[treeIndex] = e;
            return;
        }
        int mid = l + (r - l) / 2;
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);
        if (index >= mid + 1){
            set(rightTreeIndex, mid + 1, r, index, e);
        }
        else{
            set(leftTreeIndex, l, mid, index, e);
        }
        tree[treeIndex] = merge.merge(tree[leftTreeIndex],tree[rightTreeIndex]);

    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null){
                res.append(tree[i]);
            }
            else{
                res.append("null");
            }
            if (i != tree.length - 1){
                res.append(",");
            }
        }
        res.append("]");
        return res.toString();
    }

    public static void main(String[] args) {
        Integer[] nums = {1, 0, 3, -5, 2, -1};
        SegmentTree<Integer> segmentTree = new SegmentTree<>(nums, Integer::sum);
        System.out.println(segmentTree);
        System.out.println(segmentTree.query(0, 2));
        System.out.println(segmentTree.query(2, 5));
        System.out.println(segmentTree.query(0, 5));
    }
}
