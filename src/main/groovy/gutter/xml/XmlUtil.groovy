package gutter.xml

class XmlUtil {
    static boolean hasValue(Node node) {
        return node.children() instanceof NodeList && node.children() && node.children().first() instanceof String
    }
}
