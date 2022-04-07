package external;

import com.intuit.karate.junit5.Karate;

class ExternalRunner {

    /*@Karate.Test
    Karate testLogin() {
        return Karate.run("login").relativeTo(getClass());
    }    

    @Karate.Test
    Karate testWs() {
        return Karate.run("ws").relativeTo(getClass());
    }  */

    @Karate.Test
    Karate testPrincipal() {
        return Karate.run("principal").relativeTo(getClass());
    }  

    @Karate.Test
    Karate testPrincipal2() {
        return Karate.run("principal2").relativeTo(getClass());
    } 

}