package net.jmob.jsconf.core.test;

import net.jmob.jsconf.core.ConfigurationFactory;
import net.jmob.jsconf.core.sample.bean.ConfigBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

// Test required to have put ${java.io.tmpdir} on ClassPath
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class RealReloadingTest {

    private static final String TIC = "Tic";
    private static final String TAC = "Tac";

    private static Path tempDirectory;
    private static Path tempFile;

    @Autowired
    private ConfigBean conf;

    @BeforeClass
    public static void init() throws IOException {
        tempDirectory = Files.createTempDirectory("jsconf-" .concat(Long.toString(System.nanoTime())));
        tempFile = Files.createFile(Paths.get(tempDirectory.toString(), "app.conf"));
    }

    @AfterClass
    public static void clean() throws IOException {
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDirectory);
    }

    @Test
    // Test required to have put ${java.io.tmpdir} on ClassPath
    public void testRealReloading() throws IOException, InterruptedException {
        final Object ref = this.conf;
        assertNotNull(this.conf);

        assertEquals(null, this.conf.getUrl());
        write(TIC);
        assertEquals(TIC, this.conf.getUrl());
        write(TAC);
        assertEquals(TAC, this.conf.getUrl());

        assertTrue(ref == this.conf);

        Files.deleteIfExists(tempFile);
        Files.createFile(Paths.get(tempDirectory.toString(), "app.conf"));

        write(TIC);
        assertEquals(TIC, this.conf.getUrl());
    }

    private void write(String value) throws IOException, InterruptedException {
        try (FileWriter fw = new FileWriter(new File(tempFile.toUri()))) {
            fw.append("simpleConf : {  url : \"").append(value).append("\" }");
            fw.flush();
        }
        sleep(5000);
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        public static ConfigurationFactory configurationFactory() {
            return new ConfigurationFactory()
                    .withResourceName(tempDirectory.getName(tempDirectory.getNameCount() - 1) + "/app.conf")//
                    .withBean("simpleConf", ConfigBean.class, "myBeanId", true);
        }
    }
}
