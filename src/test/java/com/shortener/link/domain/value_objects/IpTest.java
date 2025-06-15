package com.shortener.link.domain.value_objects;

import com.shortener.link.domain.enums.Uf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IpTest {

    @Test
    public void testCreateIpCorrectly() {
        Ip ip = new Ip("64.97.39.220", new Location("Cidade", Uf.AC));
        Assertions.assertEquals("64.97.39.220", ip.value());
        Assertions.assertEquals("Cidade", ip.location().city());
        Assertions.assertEquals(Uf.AC, ip.location().uf());
    }

    @Test
    public void testCreateIpInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Ip(null, new Location("Cidade", Uf.AC)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Ip("", new Location("Cidade", Uf.AC)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Ip("64.97.39.220", null));
    }

    @Test
    public void testCreateLocallyOrGlobalIp() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Ip("127.0.0.1", new Location("Cidade", Uf.AC)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Ip("0.0.0.0", new Location("Cidade", Uf.AC)));
    }
}
