package com.falkordb.graph_entities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * * A class represent an node (graph entity). In addition to the base class id and properties, a node has labels.
 */
public class Node extends GraphEntity {

    //members
    private final Set<String> labels;

    public Node() {
        super();
        labels = new HashSet<>();
    }

    /**
     * Use this constructor to reduce memory allocations
     * when labels or properties are added to the node
     *
     * @param labelsCapacity     preallocate the capacity for the node labels
     * @param propertiesCapacity preallocate the capacity for the properties
     */
    public Node(int labelsCapacity, int propertiesCapacity) {
        super(propertiesCapacity);
        this.labels = new HashSet<>(labelsCapacity);
    }


    /**
     * @param label - a label to be add
     */
    public void addLabel(String label) {
        labels.add(label);
    }

    /**
     * @param label - a label to be removed
     */
    public void removeLabel(String label) {
        labels.remove(label);
    }

    /**
     * @param label
     * @return
     */
    public boolean hasLabel(String label) {
        return labels.contains(label);
    }

    public Set<String> getLabels() {
        return labels;
    }

    //    /**
//     * @param index - label index
//     * @return the property label
//     * @throws IndexOutOfBoundsException if the index is out of range
//     *                                   ({@code index < 0 || index >= getNumberOfLabels()})
//     */
//    public String getLabel(int index) {
//        return labels.get(index);
//    }

    /**
     * @return the number of labels
     */
    public int getNumberOfLabels() {
        return labels.size();
    }

    public Property get(String propertyName) {
        return propertyMap.getOrDefault(propertyName, new Property<>());
    }

    public Map<String, Object> asMap() {
        return propertyMap.values().stream().collect(Collectors.toMap(Property::getName, Property::getValue));
    }

    public Map<String, String> asMap2() {
        return propertyMap.values().stream().collect(Collectors.toMap(Property::getName, Property::asString));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;
        Node node = (Node) o;
        return Objects.equals(labels, node.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), labels);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Node{");
        sb.append("labels=").append(labels);
        sb.append(", id=").append(id);
        sb.append(", propertyMap=").append(propertyMap);
        sb.append('}');
        return sb.toString();
    }
}
