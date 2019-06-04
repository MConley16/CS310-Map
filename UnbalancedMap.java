package edu.sdsu.cs.datastructures;
	import java.util.LinkedList;
	import java.util.Iterator;
	

	public class UnbalancedMap<K extends Comparable<K>,V> implements IMap<K,V>
	{
	    private Node<K,V> root;
	    //Default constructor of an unbalanced map
	    public UnbalancedMap() {
	    }
	    //Copy constructor of an unbalancedMap
	    public UnbalancedMap(IMap toCopy)
	    {
	        Iterable keysToImport = toCopy.keyset();
	        Iterable valuesToImport = toCopy.values();
	        Iterator keyIterator = keysToImport.iterator();
	        Iterator valueIterator = valuesToImport.iterator();
	        for(int i = 0; i < toCopy.size(); i++)
	        {
	            Object currentKey = keyIterator.next();
	            Object currentValue = valueIterator.next();
	            add((K)currentKey, (V)currentValue);
	        }
	    }
	

	    //keep adding left from the source recursively until reach a null in the
	    //source and add right after each left
	

	    private class Node<K,V>
	    {
	        public K key;
	        public V value;
	        public Node left;
	        public Node right;
	

	        public Node (K key, V value)
	        {
	            this.key = key;
	            this.value = value;
	            this.left = null;
	            this.right = null;
	        }
	    }
	

	    /**
	     * Indicates if the map contains the object identified by the key inside.
	     *
	     * @param key The object to compare against
	     * @return true if the parameter object appears in the structure
	     */
	    public boolean contains(K key)
	    {
	        Node<K, V> currentNode = new Node(key, null);
	        Node<K, V> comparisonNode;
	

	        //If map is empty:
	        if(root == null)
	        {
	            return false;
	        }
	        else
	        {
	            comparisonNode = root;
	            while(comparisonNode != null)
	            {
	                //If the key we're looking for is less than the one we're
	                //comparing it to right now, keep looking to the left:
	                if (currentNode.key.compareTo(comparisonNode.key) < 0) {
	                    comparisonNode = comparisonNode.left;
	                }
	                //If the key we're looking for is greater than the one we're
	                // comparing it to right now, keep looking to the right:
	                else if (currentNode.key.compareTo(comparisonNode.key) > 0)
	                {
	                    comparisonNode = comparisonNode.right;
	                }
	                //If found it:
	                else if(comparisonNode.key.compareTo(currentNode.key) == 0)
	                {
	                    return true;
	                }
	                //If didn't find it:
	                else if(currentNode.key.compareTo(null) == 0)
	                {
	                    return false;
	                }
	            }
	        }
	        return false;
	    }
	

	    /**
	     * Adds the given key/value pair to the dictionary.
	     *
	     * @param key
	     * @param value
	     * @return false if the dictionary is full, or if the key is a duplicate.
	     * Returns true if addition succeeded.
	     */
	    public boolean add(K key, V value)
	    {
	        Node<K, V> currentNode = new Node(key, value);
	        Node<K, V> comparisonNode;
	

	        //Make the given node the root if the map is empty
	        if(root == null)
	        {
	            root = currentNode;
	        }
	        else
	        {
	            comparisonNode = root;
	            while(comparisonNode != null)
	            {
	                //If currentNode is less than the comparisonNode:
	                if(currentNode.key.compareTo(comparisonNode.key) < 0)
	                {
	                    if(comparisonNode.left == null)
	                    {
	                        comparisonNode.left =
	                                new Node(currentNode.key, currentNode.value);
	                        return true;
	                    }
	                    //If the left spot is already taken, make the left node the
	                    //comparisonNode and keep looking for an empty spot to put
	                    //it in
	                    else
	                    {
	                        comparisonNode = comparisonNode.left;
	                    }
	                }
	                //If currentNode is greater than the comparisonNode:
	                else if(currentNode.key.compareTo(comparisonNode.key) > 0)
	                {
	                    if(comparisonNode.right == null)
	                    {
	                        comparisonNode.right =
	                                new Node(currentNode.key, currentNode.value);
	                        return true;
	                    }
	                    //If the right spot is already taken, make the right node
	                    //the comparisonNode and keep looking for an empty spot
	                    // to put it in
	                    else
	                    {
	                        comparisonNode = comparisonNode.right;
	                    }
	                }
	                //Last case = both keys are equal, so don't add the given node
	                //if that key is already in the map
	                else
	                {
	                    return false;
	                }
	            }
	        }
	        return false;
	    }
	

	    /**
	     * Deletes the key/value pair identified by the key parameter.
	     *
	     * @param key
	     * @return The previous value associated with the deleted key or null if not
	     * present.
	     */
	

	    public V delete(K key)
	    {
	        V previousValue = getValue(key);
	        root = deleteHelper(root, key);
	        return previousValue;
	    }
	

	    private Node<K,V> deleteHelper(Node<K,V> currentNode, K keyOfNodeToDelete)
	    {
	        if (root == null)
	        {
	            return null;
	        }
	        if (keyOfNodeToDelete.compareTo(currentNode.key) == 0)
	        {
	            if (currentNode.left == null && currentNode.right == null)
	            {
	                return null;
	            }
	            if (currentNode.left == null)
	            {
	                return currentNode.right;
	            }
	            if (currentNode.right == null)
	            {
	                return currentNode.left;
	            }
	            Node<K,V> tmp = replacementChild(currentNode.right);
	            currentNode.key = tmp.key;
	            currentNode.value = tmp.value;
	            currentNode.right = deleteHelper(currentNode.right, tmp.key);
	            return currentNode;
	        }
	

	        else if (keyOfNodeToDelete.compareTo(currentNode.key) < 0)
	        {
	            currentNode.left = deleteHelper(currentNode.left,
	                    keyOfNodeToDelete);
	            return currentNode;
	        }
	        else
	        {
	            currentNode.right = deleteHelper(currentNode.right,
	                    keyOfNodeToDelete);
	            return currentNode;
	        }
	    }
	

	

	    private Node<K,V> replacementChild(Node<K,V> minSoFar)
	    {
	        if (minSoFar.left == null)
	        {
	            return minSoFar;
	        }
	        else
	        {
	            return replacementChild(minSoFar.left);
	        }
	    }
	

	    /**
	     * Retreives, but does not remove, the value associated with the provided
	     * key.
	     *
	     * @param key The key to identify within the map.
	     * @return The value associated with the indicated key.
	     */
	    public V getValue(K key)
	    {
	        Node<K, V> currentNode = new Node(key, null);
	        Node<K, V> comparisonNode;
	

	        //If map is empty:
	        if(root == null)
	        {
	            return null;
	        }
	        else
	        {
	            comparisonNode = root;
	            while(comparisonNode != null)
	            {
	                //If the key we're looking for is less than the one we're
	                //comparing it to right now, keep looking to the left:
	                if (currentNode.key.compareTo(comparisonNode.key) < 0)
	                {
	                    comparisonNode = comparisonNode.left;
	                }
	                //If the key we're looking for is greater than the one we're
	                //comparing it to right now, keep looking to the right:
	                else if (currentNode.key.compareTo(comparisonNode.key) > 0)
	                {
	                    comparisonNode = comparisonNode.right;
	                }
	                //If found it:
	                else if(comparisonNode.key.compareTo(currentNode.key) == 0)
	                {
	                    return comparisonNode.value;
	                }
	                //If didn't find it:
	                else if(currentNode.key.compareTo(null) == 0)
	                {
	                    return null;
	                }
	            }
	        }
	        return null;
	    }
	

	    /**
	     * Returns a key in the map associated with the provided value.
	     *
	     * @param value The value to find within the map.
	     * @return The first key found associated with the indicated value.
	     */
	    public K getKey(V value)
	    {
	        LinkedList<K> valueList = new LinkedList<>();
	        K theKey = null;
	

	        //If map is empty:
	        if(root == null)
	        {
	            //return null;
	            valueList.add(null);
	            return valueList.get(0);
	        }
	        else {
	            getKeyHelper(valueList, root, value, theKey);
	        }
	        if(valueList.isEmpty())
	        {
	            return null;
	        }
	

	        return valueList.get(0);
	    }
	

	    private void getKeyHelper(LinkedList<K> valueList, Node currentNode,
	                              V theValue, K theKey)
	    {
	        if(currentNode.left != null)
	        {
	            getKeyHelper(valueList, currentNode.left, theValue, theKey);
	        }
	

	        if(currentNode.value.equals(theValue))
	        {
	            valueList.add((K)currentNode.key);
	            theKey = (K)currentNode.key;
	        }
	

	        if(currentNode.right != null)
	        {
	            getKeyHelper(valueList, currentNode.right, theValue, theKey);
	        }
	    }
	

	    /**
	     * Returns all keys associated with the indicated value contained within the
	     * map.
	     *
	     * @param value The value to locate within the map.
	     * @return An iterable object containing all keys associated with the
	     * provided value.
	     */
	    public Iterable<K> getKeys(V value)
	    {
	        LinkedList<K> valueList = new LinkedList<>();
	        K theKey = null;
	

	        //If map is empty:
	        if(root == null)
	        {
	            return valueList;
	            //return null;
	        }
	        getKeyHelper(valueList, root, value, theKey);
	        return valueList;
	    }
	

	

	    /**
	     * Indicates the count of key/value entries stored inside the map.
	     *
	     * @return A non-negative number representing the number of entries.
	     */
	    public int size()
	    {
	        LinkedList<K> orderedKeySet = new LinkedList<>();
	        if(root == null)
	        {
	            return 0;
	        }
	        else
	        {
	            sizeHelper(orderedKeySet, root);
	            return orderedKeySet.size();
	        }
	    }
	

	    private void sizeHelper(LinkedList<K> set, Node currentNode)
	    {
	        if(currentNode.left != null)
	        {
	            keySetHelper(set, currentNode.left);
	        }
	

	        set.add((K)currentNode.key);
	        if(currentNode.right != null)
	        {
	            keySetHelper(set,currentNode.right);
	        }
	    }
	

	    /**
	     * Indicates if the dictionary contains any items.
	     *
	     * @return true if the dictionary is empty, false otherwise.
	     */
	    public boolean isEmpty()
	    {
	        if(root == null)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	

	    }
	

	    /***
	     * Returns the map to an empty state ready to accept new entries.
	     */
	    public void clear()
	    {
	        clearHelper(root);
	    }
	

	    private void clearHelper(Node currentNode) {
	        if (currentNode == root) {
	            root = null;
	        }
	        if (currentNode.left != null) {
	            clearHelper(currentNode.left);
	

	        }
	        if (currentNode.right != null) {
	            clearHelper(currentNode.right);
	        }
	        currentNode = null;
	    }
	

	

	    /**
	     * Provides an Iterable object of the keys in the dictionary.
	     * <p>
	     * The keys provided by this method must appear in their natural, ascending,
	     * order.
	     *
	     * @return An iterable set of keys.
	     */
	    public Iterable<K> keyset()
	    {
	        LinkedList<K> orderedKeySet = new LinkedList<>();
	        if(root == null)
	        {
	            //return null;
	            return orderedKeySet;
	        }
	        keySetHelper(orderedKeySet, root);
	        return orderedKeySet;
	    }
	

	    private void keySetHelper(LinkedList<K> set, Node currentNode)
	    {
	        if(currentNode.left != null)
	        {
	            keySetHelper(set, currentNode.left);
	        }
	

	        set.add((K)currentNode.key);
	        if(currentNode.right != null)
	        {
	            keySetHelper(set,currentNode.right);
	        }
	    }
	

	    /**
	     * Provides an Iterable object of the keys in the dictionary.
	     * <p>
	     * The values provided by this method must appear in an order matching the
	     * keyset() method. This object may include duplicates if the data structure
	     * includes duplicate values.
	     *
	     * @return An iterable object of all the dictionary's values.
	     */
	    public Iterable<V> values()
	    {
	        LinkedList<V> orderedValueSet = new LinkedList<>();
	        if(root == null)
	        {
	            return orderedValueSet;
	        }
	        valuesHelper(orderedValueSet, root);
	        return orderedValueSet;
	    }
	

	    private void valuesHelper(LinkedList<V> set, Node currentNode)
	    {
	        if(currentNode.left != null)
	        {
	            valuesHelper(set, currentNode.left);
	        }
	

	        set.add((V)currentNode.value);
	        if(currentNode.right != null)
	        {
	            valuesHelper(set,currentNode.right);
	        }
	    }
	}

