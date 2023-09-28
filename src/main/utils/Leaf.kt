package utils


class Leaf<T>(val value: T, var parent: Leaf<T>?) {
    val children: MutableList<Leaf<T>> = mutableListOf()
    val depth: Int get() = if (parent == null) 0 else parent!!.depth + 1

    fun addChild(leaf: Leaf<T>) {
        if (leaf.parent == null) {
            leaf.parent = this
        }
        children.add(leaf)

    }

    fun removeChild(leaf: Leaf<T>) = children.remove(leaf)

    fun getAllLeafs(): List<Leaf<T>> {
        val list = mutableListOf(this)

        for (child in children) {
            list.addAll(child.getAllLeafs())
        }

        return list
    }

    /**
     * Returns a list with the leafs from the root to this leaf
     */
    fun pathToLeaf(): List<Leaf<T>> {
        val path = listOf(this)

        return pathToLeaf(path)
    }

    private fun pathToLeaf(path: List<Leaf<T>>): List<Leaf<T>> {
        val newPath = path.toMutableList()
        newPath.add(this)

        if (parent == null) {
            return newPath
        }

        return parent!!.pathToLeaf(newPath)
    }

    /**
     * Check if an element is present in this branch looking up
     */
    fun includesUp(element: T): Boolean {
        if (this.value == element) {
            return true
        }
        if (this.parent != null) {
            return parent!!.includesUp(element)
        }

        return false
    }

    /**
     * Check if an element is present in this tree down from this leaf
     */
    fun includesDown(element: T): Boolean {
        if (this.value == element) {
            return true
        }

        val includes = this.children.map { it.includesDown(element) }

        return includes.any()
    }

    fun getEndLeafs(): List<Leaf<T>> {
        if (children.size == 0) {
            return emptyList()
        }

        val childrenEndLeafs = mutableListOf<Leaf<T>>()

        for (child in children) {
            if (child.children.size == 0) {
                childrenEndLeafs.add(child)
            } else {
                childrenEndLeafs.addAll(child.getEndLeafs())
            }
        }

        return childrenEndLeafs
    }

    fun getEndLeafs(predicate: (T) -> Boolean): List<Leaf<T>> {
        return getEndLeafs().filter { predicate(it.value) }
    }
}
