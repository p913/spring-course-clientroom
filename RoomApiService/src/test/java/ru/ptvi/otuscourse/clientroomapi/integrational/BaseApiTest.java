package ru.ptvi.otuscourse.clientroomapi.integrational;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Интеграционный тест, заточенный на работу с объектами лежащими в in-memory БД H2, см. liquibase changesets
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseApiTest {
    public static final String CONTRAGENT_ID_EXISTS = "026bb306-139b-4b9c-b54a-65ec3def7e88";
    public static final String CONTRAGENT_ID_EXISTS_EMAIL = "petrov@mail.ru";
    public static final String CONTRAGENT_ID_NON_EXISTS = "945ae326-6cd9-4cb2-bd43-e478fdbe60e1";
    public static final String CONTRAGENT_ID_WITHOUT_REFERECNES = "f6281e7f-c6ca-4f9c-9772-6effb020b408";

    public static final String CONTRACT_ID_EXISTS = "5a9836ff-ebae-454e-ad95-5fa2def408de";
    public static final String CONTRACT_ID_NON_EXISTS = "1fb666bc-659e-460a-8d4e-0eadfb82581b";
    public static final String CONTRACT_ID_WITHOUT_REFERENCES = "3e754779-bf8c-4b22-9ddd-1e2227681b54";

    public static final String ACCOBJECT_ID_EXISTS = "da2ea746-a274-441e-978a-ee661d9b77fd";
    public static final String ACCOBJECT_ID_NON_EXISTS = "bded100e-416e-4289-aa2c-77a96137869c";
    public static final String ACCOBJECT_ID_WITHOUT_REFERENCES = "d9fb833e-5654-48f2-98ca-ce96035d52fb";

    public static final String SERVICE_ID_EXISTS = "da17d145-3c06-4fac-aa1c-3b7c8bedf28a";
    public static final String SERVICE_ID_NON_EXISTS = "3481e326-659e-460a-8d4e-6effb020b509";
    public static final String SERVICE_ID_WITHOUT_REFERENCES = "c919468c-6a91-4f11-9333-3badda9d33cb";

    public static final String DOCUMENT_ID_EXISTS = "6b69ff1b-6250-41b8-9cfc-109a42f81852";
    public static final String DOCUMENT_ID_NON_EXISTS = "21c09334-5504-41e8-98ed-3816f0d1a577";

    public static final String PAY_ID_EXISTS = "22eb843b-5a3a-4acc-ba22-45f8bc1b86d7";
    public static final String PAY_ID_NON_EXISTS = "f4abc5c7-5c38-468f-ae3a-76ce326a674d";

    public static final String NOTIFICATION_ID_EXISTS = "03a57169-e02b-4388-b6f0-e7032789b2c7";
    public static final String NOTIFICATION_ID_NON_EXISTS = "c70ce20d-3dc5-4f51-9db9-984715a1378b";

    public static final String DEMAND_ID_EXISTS = "623a8d47-487a-43be-8df8-d96966f6969c";
    public static final String DEMAND_ID_NON_EXISTS = "74807747-b7f5-442c-b32d-801e8de527bc";

    public static final String CHARGE_ONCE_ID_EXISTS = "6a3b3c83-1d16-4913-b56c-c778f957713a";
    public static final String CHARGE_ONCE_ID_NON_EXISTS = "9dc93c2c-e880-4f69-a22f-979b714ff4fc";

    public static final String CHARGE_SERVICE_ID_EXISTS = "11512a0f-c489-43b4-a577-b07f9e3e60d6";
    public static final String CHARGE_SERVICE_ID_NON_EXISTS = "8c09577f-6dc0-498e-8feb-abef65215ab2";

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ObjectMapper jsonMapper;

}
