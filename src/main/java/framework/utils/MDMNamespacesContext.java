package framework.utils;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class MDMNamespacesContext implements NamespaceContext {
        public String getNamespaceURI(String prefix) {
            switch (prefix){
                case "prd": return "http://ns.antel.com.uy/schema/mdm/db/sid/base/product-v1";
                case "cmn": return "http://ns.antel.com.uy/schema/mdm/db/sid/base/common-v1";
                case "xsi": return "http://www.w3.org/2001/XMLSchema-instance";

                default: return null;
            }
        }
        public Iterator getPrefixes(String val) {
            return null;
        }
        public String getPrefix(String uri) {
            return null;
        }
    }

