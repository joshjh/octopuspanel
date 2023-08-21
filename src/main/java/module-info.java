module com.rasp.octopuspanel {
    requires org.slf4j;             //slf4j-api-2.0.0-alpha1.jar
    requires org.slf4j.simple;      //slf4j-simple-2.0.0-alpha1.jar & simplelogger.properties
    requires com.pi4j;
    requires com.pi4j.plugin.raspberrypi;
    // requires com.pi4j.plugin.pigpio;
    // requires com.pi4j.library.pigpio;
    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;
    requires org.json;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5;
    // allow access to classes in the following namespaces for Pi4J annotation processing
    opens octopuspanel to com.pi4j;

    //exports octopuspanel;
}