module com.rasp.octopuspanel {
    requires org.slf4j;             //slf4j-api-2.0.0-alpha1.jar
    requires org.slf4j.simple;      //slf4j-simple-2.0.0-alpha1.jar & simplelogger.properties
    requires transitive com.pi4j;
    requires transitive com.pi4j.plugin.raspberrypi;
    // requires com.pi4j.plugin.pigpio;
    // requires com.pi4j.library.pigpio;
    requires transitive org.json;
    requires transitive org.apache.httpcomponents.httpclient;
    requires transitive org.apache.httpcomponents.httpcore;
    requires transitive org.apache.httpcomponents.core5.httpcore5;
    requires transitive org.apache.httpcomponents.client5.httpclient5;
    requires transitive com.raspb.agilelightcycle;
    // allow access to classes in the following namespaces for Pi4J annotation processing
    opens octopuspanel to com.pi4j;

    exports octopuspanel;
}