package com.rined.blog.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.rined.blog.model.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"unused"})
@ChangeLog(order = "000")
public class MongoDBChangeLog0 {
    private User rined;
    private User noname;

    private Map<String, Tag> tagMap = new HashMap<>();
    private List<Tag> tagList = new ArrayList<>();
    private Random random = new Random();


    @ChangeSet(order = "000", id = "dropDB", author = "rined", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initUsers", author = "rined", runAlways = true)
    public void initUsers(MongoTemplate template) {
        rined = template.save(new User(
                "Rined",
                LocalDate.of(2012, 1, 1),
                LocalDateTime.now(),
                new UserInfo("Cool man!", "lol@fun.true", "Anton", "Petrov"),
                10
        ));
        noname = template.save(new User(
                "Noname",
                LocalDate.now(),
                LocalDateTime.now(),
                11
        ));

        for (int i = 0; i < random.nextInt(500); i++) {
            template.save(new User(
                    UUID.randomUUID().toString(),
                    LocalDate.now(),
                    LocalDateTime.now(),
                    (int) (Math.random() * 10)
            ));
        }
    }

    @ChangeSet(order = "002", id = "initTags", author = "rined", runAlways = true)
    public void initTags(MongoTemplate template) {
        initializeMap(template, "JPA");
        initializeMap(template, "JAXB");
        initializeMap(template, "JavaEE");
        initializeMap(template, "Spring");
//        initializeMap(template, "EJB");
//        initializeMap(template, "MongoDB");
//        initializeMap(template, "Servlet");
//        initializeMap(template, "JSP");
//        initializeMap(template, "JSF");
//        initializeMap(template, "Struts");
//        initializeMap(template, "JNDI");
//        initializeMap(template, "JMS");
//        initializeMap(template, "JMH");
//        initializeMap(template, "GUI");
//        initializeMap(template, "JOOQ");
//        initializeMap(template, "Security");
//        initializeMap(template, "Cryptography");
//        initializeMap(template, "Redis");
//        initializeMap(template, "IoC");
//        initializeMap(template, "JDBC");
//        initializeMap(template, "Algorithms");
//        initializeMap(template, "SQL");
//        initializeMap(template, "XSLT");
//        initializeMap(template, "Vaadin");
//        initializeMap(template, "Garbage-Collector");
//        initializeMap(template, "JAX-RS");
//        initializeMap(template, "JAX-WS");
//        initializeMap(template, "Regex");
//        initializeMap(template, "Spring Boot");
//        initializeMap(template, "Spring Data");
//        initializeMap(template, "Spring MVC");
//        initializeMap(template, "Spring Security");
//        initializeMap(template, "XPath");
//        initializeMap(template, "JSON");
//        initializeMap(template, "AOP");
//        initializeMap(template, "Bean-validation");
//        initializeMap(template, "CLI");
//        initializeMap(template, "Cloud");
//        initializeMap(template, "CSV");
//        initializeMap(template, "Hibernate");
//        initializeMap(template, "Docker");
//        initializeMap(template, "Git");
//        initializeMap(template, "HTML");
//        initializeMap(template, "JS");
//        initializeMap(template, "JAXP");
//        initializeMap(template, "YAML");
//        initializeMap(template, "SOAP");
//        initializeMap(template, "Spring Shell");
//        initializeMap(template, "React");
//        initializeMap(template, "ElectronJS");
//        initializeMap(template, "Angular");
//        initializeMap(template, "NoSQL");
//        initializeMap(template, "Spring Cache");
//        initializeMap(template, "SPEL");
//        initializeMap(template, "RxJava");
//        initializeMap(template, "MQ");
//        initializeMap(template, "JTA");
    }

    @ChangeSet(order = "003", id = "initTopics", author = "rined", runAlways = true)
    public void initTopics(MongoTemplate template) {
        TopicInfo jpaTopicInfo = new TopicInfo("Awesome JPA",
                Arrays.asList("jpa", "one-to-one"),
                Arrays.asList(tagMap.get("JPA"), tagMap.get("JavaEE"), tagMap.get("Spring")),
                rined
        );
        template.save(new Topic(
                jpaTopicInfo,
                new Content("Interesting topic about JPA"),
                LocalDateTime.now(),
                15,
                Arrays.asList(
                        new Comment(
                                rined,
                                "WOW!",
                                LocalDateTime.now(),
                                0
                        ),
                        new Comment(
                                noname,
                                "So useful!",
                                LocalDateTime.of(2014, 1, 1, 0, 0), 0)
                )));

        TopicInfo jaxbTopicInfo = new TopicInfo("Amazing JAXB",
                Arrays.asList("jaxb", "xml-object-mapping"),
                Arrays.asList(tagMap.get("JAXB"), tagMap.get("JavaEE")),
                rined);

        template.save(new Topic(
                jaxbTopicInfo,
                new Content("Interesting topic about JAXB"),
                LocalDateTime.of(2013, 1, 1, 0, 0),
                14,
                Arrays.asList(
                        new Comment(rined, "So bad...", LocalDateTime.now().minusHours(10), -5),
                        new Comment(rined, "Why you minus me? It is really bad!", LocalDateTime.now(), -7)
                )
        ));

        for (int i = 0; i < 10; i++) {
            List<Tag> randomTags = randomTags(random.nextInt(4) + 1);
            template.save(new Topic(
                    new TopicInfo(
                            "About " + randomTags.get(0).getTag() + " " + UUID.randomUUID().toString(),
                            Arrays.asList("jaxb", "xml-object-mapping"),
                            randomTags,
                            random.nextBoolean() ? rined : noname
                    ),
                    new Content("Empty content!"),
                    LocalDateTime.of(
                            2013, random.nextInt(11) + 1,
                            random.nextInt(28) + 1,
                            random.nextInt(24),
                            random.nextInt(60)
                    ),
                    (random.nextBoolean() ? 1 : -1) * random.nextInt(10),
                    Collections.singletonList(
                            new Comment(
                                    random.nextBoolean() ? rined : noname,
                                    random.nextBoolean() ? "Best!" : "Worst!",
                                    LocalDateTime.now().minusHours(random.nextInt(12)),
                                    random.nextInt(4)
                            )
                    )
            ));
        }
    }

    private void initializeMap(MongoTemplate template, String tag) {
        Tag save = template.save(new Tag(tag));
        tagMap.put(tag, save);
        tagList.add(save);
    }

    private List<Tag> randomTags(int number) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            tags.add(tagList.get(random.nextInt(tagList.size())));
        }
        return tags;
    }
}
