package es.macero.cqgame.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import es.macero.cqgame.domain.users.SonarUser;
import es.macero.cqgame.domain.users.SonarUserList;

@Repository
public class SonarUserRepository
{

    private SonarUserList list;

    @Value("${userDataFile}")
    private String userDataFile;

    @PostConstruct
    public void init() throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance(SonarUserList.class);
        list = (SonarUserList) context.createUnmarshaller().unmarshal(getClass().getClassLoader().getResourceAsStream("data/" + userDataFile));
    }

    public List<SonarUser> findAll()
    {
        return list.getUsers();
    }
}
