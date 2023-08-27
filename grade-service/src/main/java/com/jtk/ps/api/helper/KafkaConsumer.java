package com.jtk.ps.api.helper;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.ps.api.dto.kafka.AccountKafka;
import com.jtk.ps.api.dto.kafka.AssessmentAspectKafka;
import com.jtk.ps.api.dto.kafka.CompanyKafka;
import com.jtk.ps.api.dto.kafka.DeadlineKafka;
import com.jtk.ps.api.dto.kafka.EvaluationKafka;
import com.jtk.ps.api.dto.kafka.FinalMappingKafka;
import com.jtk.ps.api.dto.kafka.LecturerKafka;
import com.jtk.ps.api.dto.kafka.ParticipantKafka;
import com.jtk.ps.api.dto.kafka.PrerequisiteKafka;
import com.jtk.ps.api.dto.kafka.SelfAssessmentAspectKafka;
import com.jtk.ps.api.dto.kafka.SelfAssessmentGradeKafka;
import com.jtk.ps.api.dto.kafka.SelfAssessmentKafka;
import com.jtk.ps.api.dto.kafka.SupervisorGradeAspectKafka;
import com.jtk.ps.api.dto.kafka.SupervisorGradeKafka;
import com.jtk.ps.api.dto.kafka.SupervisorGradeResultKafka;
import com.jtk.ps.api.dto.kafka.SupervisorMappingKafka;
import com.jtk.ps.api.dto.kafka.TimelineKafka;
import com.jtk.ps.api.dto.kafka.ValuationKafka;
import com.jtk.ps.api.model.Account;
import com.jtk.ps.api.model.AssessmentAspect;
import com.jtk.ps.api.model.Company;
import com.jtk.ps.api.model.Deadline;
import com.jtk.ps.api.model.Evaluation;
import com.jtk.ps.api.model.EvaluationForm;
import com.jtk.ps.api.model.EventStore;
import com.jtk.ps.api.model.FinalMapping;
import com.jtk.ps.api.model.Lecturer;
import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.Prerequisite;
import com.jtk.ps.api.model.SelfAssessment;
import com.jtk.ps.api.model.SelfAssessmentAspect;
import com.jtk.ps.api.model.SelfAssessmentGrade;
import com.jtk.ps.api.model.SupervisorGrade;
import com.jtk.ps.api.model.SupervisorGradeAspect;
import com.jtk.ps.api.model.SupervisorGradeResult;
import com.jtk.ps.api.model.SupervisorMapping;
import com.jtk.ps.api.model.Timeline;
import com.jtk.ps.api.model.Valuation;
import com.jtk.ps.api.repository.AccountRepository;
import com.jtk.ps.api.repository.AssessmentAspectRepository;
import com.jtk.ps.api.repository.CompanyRepository;
import com.jtk.ps.api.repository.CriteriaComponentCourseRepository;
import com.jtk.ps.api.repository.DeadlineRepository;
import com.jtk.ps.api.repository.EvaluationFormRepository;
import com.jtk.ps.api.repository.EvaluationRepository;
import com.jtk.ps.api.repository.EventStoreRepository;
import com.jtk.ps.api.repository.FinalMappingRepository;
import com.jtk.ps.api.repository.LecturerRepository;
import com.jtk.ps.api.repository.ParticipantRepository;
import com.jtk.ps.api.repository.PrerequisiteRepository;
import com.jtk.ps.api.repository.SelfAssessmentAspectRepository;
import com.jtk.ps.api.repository.SelfAssessmentGradeRepository;
import com.jtk.ps.api.repository.SelfAssessmentRepository;
import com.jtk.ps.api.repository.SupervisorGradeAspectRepository;
import com.jtk.ps.api.repository.SupervisorGradeRepository;
import com.jtk.ps.api.repository.SupervisorGradeResultRepository;
import com.jtk.ps.api.repository.SupervisorMappingRepository;
import com.jtk.ps.api.repository.TimelineRepository;
import com.jtk.ps.api.repository.ValuationRepository;

@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private static final String groupId = "myGroup3";

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private DeadlineRepository deadlineRepository;

    @Autowired
    @Lazy
    private LecturerRepository lecturerRepository;

    @Autowired
    @Lazy
    private CompanyRepository companyRepository;

    @Autowired
    @Lazy
    private EvaluationRepository evaluationRepository;

    @Autowired
    @Lazy
    private ValuationRepository valuationRepository;

    @Autowired
    @Lazy
    private CriteriaComponentCourseRepository criteriaComponentCourseRepository;

    @Autowired
    @Lazy
    private AssessmentAspectRepository assessmentAspectRepository;

    @Autowired
    @Lazy
    private TimelineRepository timelineRepository;

    @Autowired
    @Lazy
    private ParticipantRepository participantRepository;

    @Autowired
    @Lazy
    private FinalMappingRepository finalMappingRepository;

    @Autowired
    @Lazy
    private SelfAssessmentRepository selfAssessmentRepository;

    @Autowired
    @Lazy
    private SelfAssessmentGradeRepository selfAssessmentGradeRepository;

    @Autowired
    @Lazy
    private SelfAssessmentAspectRepository selfAssessmentAspectRepository;

    @Autowired
    @Lazy
    private SupervisorGradeRepository supervisorGradeRepository;

    @Autowired
    @Lazy
    private SupervisorGradeResultRepository supervisorGradeResultRepository;

    @Autowired
    @Lazy
    private SupervisorGradeAspectRepository supervisorGradeAspectRepository;

    @Autowired
    @Lazy
    private SupervisorMappingRepository supervisorMappingRepository;

    @Autowired
    @Lazy
    private EventStoreRepository eventStoreRepository;

    @Autowired
    @Lazy
    private EvaluationFormRepository evaluationFormRepository;

    @Autowired
    @Lazy
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void eventStoreHandler(String entityId, String eventType, Object object, Integer eventDataId) {
        try {
            EventStore eventStore = new EventStore();

            eventStore.setEntityId(entityId);
            eventStore.setEventType(eventType);
            eventStore.setEventTime(LocalDateTime.now());
            eventStore.setEventData(objectMapper.writeValueAsString(object));
            eventStore.setEventDataId(eventDataId);

            eventStoreRepository.save(eventStore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "account_topic", groupId = groupId)
    public void consumeAccountService(String message) {
        LOGGER.info(String.format("Message received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            AccountKafka receivedObject = objectMapper.readValue(message, AccountKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Username: " + receivedObject.getUsername());
            System.out.println("Role Id: " + receivedObject.getRole_id());
            System.out.println("Operation: " + receivedObject.getOperation());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Account account = new Account();
                account.setRole_id(receivedObject.getRole_id());
                account.setUsername(receivedObject.getUsername());
                account.setId(receivedObject.getId());
                account.setIs_delete(0);

                accountRepository.save(account);
                eventStoreHandler("account", "ACCOUNT_ADDED", account, account.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                Optional<Account> account = accountRepository.findById(receivedObject.getId());
                account.ifPresent(c -> {
                    c.setRole_id(receivedObject.getRole_id());
                    accountRepository.save(c);
                    eventStoreHandler("account", "ACCOUNT_UPDATE", c, c.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                Optional<Account> account = accountRepository.findById(receivedObject.getId());

                account.ifPresent(c -> {
                    c.setIs_delete(1);
                    accountRepository.save(c);
                    eventStoreHandler("account", "ACCOUNT_DELETE", c, c.getId());
                });

            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "company_topic", groupId = groupId)
    public void consumeCompany(String message) {
        LOGGER.info(String.format("Message company received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            CompanyKafka receivedObject = objectMapper.readValue(message, CompanyKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Company Name: " + receivedObject.getCompany_name());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Company company = new Company();
                Optional<Account> account = accountRepository.findById(receivedObject.getAccount_id());
                company.setAccount(account.get());
                company.setCompanyEmail(receivedObject.getCompany_email());
                company.setCompanyName(receivedObject.getCompany_name());
                company.setSinceYear(receivedObject.getSince_year());
                company.setStatus(receivedObject.isStatus() ? 1 : 0);
                company.setId(receivedObject.getId());

                companyRepository.save(company);
                eventStoreHandler("company", "COMPANY_ADDED", company, company.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                Optional<Company> company = companyRepository.findById(receivedObject.getId());
                company.ifPresent(c -> {
                    Optional<Account> account = accountRepository.findById(receivedObject.getAccount_id());
                    c.setAccount(account.get());
                    c.setCompanyEmail(receivedObject.getCompany_email());
                    c.setCompanyName(receivedObject.getCompany_name());
                    c.setSinceYear(receivedObject.getSince_year());
                    c.setStatus(receivedObject.isStatus() ? 1 : 0);
                    companyRepository.save(c);
                    eventStoreHandler("company", "COMPANY_UPDATE", c, c.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "evaluation_topic", groupId = groupId)
    public void consumeEvaluation(String message) {
        LOGGER.info(String.format("Message Evaluation received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            EvaluationKafka receivedObject = objectMapper.readValue(message, EvaluationKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Evaluation evaluation = new Evaluation();
                evaluation.setComment(receivedObject.getComment());
                Optional<Company> company = companyRepository.findById(receivedObject.getCompany_id());
                evaluation.setCompany(company.get());
                evaluation.setNumEvaluation(receivedObject.getNum_evaluation());
                Optional<Participant> participant = participantRepository.findById(receivedObject.getParticipant_id());
                evaluation.setParticipant(participant.get());
                evaluation.setPosition(receivedObject.getPosition());
                evaluation.setProdiId(receivedObject.getProdi_id());
                evaluation.setStatus(receivedObject.getStatus());
                evaluation.setYear(receivedObject.getYear());
                evaluation.setUpdateDate(LocalDateTime.now());

                evaluationRepository.save(evaluation);
                eventStoreHandler("evaluation", "EVALUATION_ADDED", evaluation, evaluation.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                Optional<Evaluation> evaluation = evaluationRepository.findById(receivedObject.getId());
                evaluation.ifPresent(c -> {
                    c.setComment(receivedObject.getComment());
                    Optional<Company> company = companyRepository.findById(receivedObject.getCompany_id());
                    c.setCompany(company.get());
                    c.setNumEvaluation(receivedObject.getNum_evaluation());
                    Optional<Participant> participant = participantRepository
                            .findById(receivedObject.getParticipant_id());
                    c.setParticipant(participant.get());
                    c.setPosition(receivedObject.getPosition());
                    c.setProdiId(receivedObject.getProdi_id());
                    c.setStatus(receivedObject.getStatus());
                    c.setYear(receivedObject.getYear());
                    c.setId(receivedObject.getId());
                    c.setUpdateDate(LocalDateTime.now());

                    evaluationRepository.save(c);
                    eventStoreHandler("evaluation", "EVALUATION_UPDATE", c, c.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "valuation_topic", groupId = groupId)
    public void consumeValuation(String message) {
        LOGGER.info(String.format("Message Valuation received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            ValuationKafka receivedObject = objectMapper.readValue(message, ValuationKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("aspect name: " + receivedObject.getAspectName());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Valuation valuation = new Valuation();

                valuation.setAspectName(receivedObject.getAspectName());
                Optional<Evaluation> evaluation = evaluationRepository.findById(receivedObject.getEvaluation_id());
                valuation.setEvaluation(evaluation.get());
                valuation.setIsCore(receivedObject.is_core() ? 1 : 0);
                valuation.setValue(receivedObject.getValue());

                valuationRepository.save(valuation);
                evaluationRepository.findById(valuation.getEvaluation().getId()).ifPresent(e -> {
                    e.setUpdateDate(LocalDateTime.now());
                    evaluationRepository.save(e);
                    eventStoreHandler("evaluation", "EVALUATION_UPDATE", e, e.getId());
                });
                eventStoreHandler("valuation", "VALUATION_ADDED", valuation, valuation.getId());

            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {

            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "assessment_aspect_topic", groupId = groupId)
    public void consumeAssessmentAspect(String message) {
        LOGGER.info(String.format("Message Assessment Aspect received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            AssessmentAspectKafka receivedObject = objectMapper.readValue(message, AssessmentAspectKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                AssessmentAspect aspect = new AssessmentAspect();

                aspect.setAspectName(receivedObject.getAspect_name());
                Optional<EvaluationForm> evaluationForm = evaluationFormRepository
                        .findById(receivedObject.getEvaluation_form_id());
                aspect.setEvaluationForm(evaluationForm.get());
                aspect.setIsDelete(0);
                aspect.setId(receivedObject.getId());

                assessmentAspectRepository.save(aspect);

                eventStoreHandler("assessment_aspect", "ASSESSMENT_ASPECT_ADDED", aspect, aspect.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                assessmentAspectRepository.findById(receivedObject.getId()).ifPresent(c -> {
                    c.setAspectName(receivedObject.getAspect_name());
                    Optional<EvaluationForm> evaluationForm = evaluationFormRepository
                            .findById(receivedObject.getEvaluation_form_id());
                    c.setEvaluationForm(evaluationForm.get());
                    assessmentAspectRepository.save(c);
                    eventStoreHandler("assessment_aspect", "ASSESSMENT_ASPECT_UPDATE", c, c.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                assessmentAspectRepository.findById(receivedObject.getId()).ifPresent(c -> {
                    if (criteriaComponentCourseRepository.isCriteriaByIndustryIdExist(receivedObject.getId()) == 1) {
                        criteriaComponentCourseRepository.findByIndustryId(receivedObject.getId()).forEach(cr -> {
                            cr.setIsDeleted(1);
                            criteriaComponentCourseRepository.save(cr);
                            eventStoreHandler("criteria_component_course", "CRITERIA_COMPONEN_COURSE_UPDATE", cr,
                                    cr.getId());
                        });
                        c.setIsDelete(1);
                        assessmentAspectRepository.save(c);
                        eventStoreHandler("assessment_aspect", "ASSESSMENT_ASPECT_DELETE", c, c.getId());
                    } else {
                        assessmentAspectRepository.deleteById(c.getId());
                        eventStoreHandler("assessment_aspect", "ASSESSMENT_ASPECT_DELETE", c, c.getId());
                    }
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "timeline_topic", groupId = groupId)
    public void consumeTimeline(String message) {
        LOGGER.info(String.format("Message Timeline received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            TimelineKafka receivedObject = objectMapper.readValue(message, TimelineKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------=========================");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Timeline timeline = new Timeline();
                timeline.setDescription(receivedObject.getDescription());
                timeline.setEndDate(receivedObject.getEnd_date());
                timeline.setStartDate(receivedObject.getStart_date());
                timeline.setName(receivedObject.getName());
                timeline.setProdiId(receivedObject.getProdi_id());
                timeline.setId(receivedObject.getId());

                timelineRepository.save(timeline);
                eventStoreHandler("timeline_setting", "TIMELINE_SETTING_ADDED", timeline, timeline.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                timelineRepository.findById(receivedObject.getId()).ifPresent(c -> {
                    c.setDescription(receivedObject.getDescription());
                    c.setEndDate(receivedObject.getEnd_date());
                    c.setStartDate(receivedObject.getStart_date());
                    c.setName(receivedObject.getName());
                    c.setProdiId(receivedObject.getProdi_id());

                    timelineRepository.save(c);
                    eventStoreHandler("timeline_setting", "TIMELINE_SETTING_UPDATE", c, c.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                timelineRepository.findById(receivedObject.getId()).ifPresent(c -> {
                    timelineRepository.delete(c);
                    eventStoreHandler("timeline_setting", "TIMELINE_SETTING_DELETE", c, c.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "participant_topic", groupId = groupId)
    public void consumeParticipant(String message) {
        LOGGER.info(String.format("Message Participant received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            ParticipantKafka receivedObject = objectMapper.readValue(message, ParticipantKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Participant participant = new Participant();
                Optional<Account> account = accountRepository.findById(receivedObject.getAccount_id());
                participant.setAccount(account.get());
                participant.setId(receivedObject.getId());
                participant.setName(receivedObject.getName());
                participant.setProdiId(receivedObject.getProdi_id());
                participant.setStatusCv(receivedObject.isStatus_cv() ? 1 : 0);
                participant.setYear(receivedObject.getYear());

                participantRepository.save(participant);
                eventStoreHandler("participant", "PARTICIPANT_ADDED", participant, participant.getId());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "final_mapping_topic", groupId = groupId)
    public void consumeFinalMapping(String message) {
        LOGGER.info(String.format("Message Final Mapping received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            FinalMappingKafka receivedObject = objectMapper.readValue(message, FinalMappingKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                FinalMapping finalMapping = new FinalMapping();

                Optional<Company> company = companyRepository.findById(receivedObject.getCompany_id());
                finalMapping.setCompany(company.get());
                finalMapping.setId(receivedObject.getId());
                Optional<Participant> participant = participantRepository.findById(receivedObject.getParticipant_id());
                finalMapping.setParticipant(participant.get());
                finalMapping.setProdiId(receivedObject.getProdi_id());
                finalMapping.setYear(receivedObject.getYear());

                finalMappingRepository.save(finalMapping);

                eventStoreHandler("final_mapping", "FINAL_MAPPING_ADDED", finalMapping, finalMapping.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                finalMappingRepository.findById(receivedObject.getId()).ifPresent(fm -> {
                    finalMappingRepository.delete(fm);
                    eventStoreHandler("final_mapping", "FINAL_MAPPING_DELETE", fm, fm.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "self_assessment_topic", groupId = groupId)
    public void consumeSelfAssessmentForm(String message) {
        LOGGER.info(String.format("Message Self Assessment received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SelfAssessmentKafka receivedObject = objectMapper.readValue(message, SelfAssessmentKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SelfAssessment selfAssessment = new SelfAssessment();

                selfAssessment.setFinishDate(receivedObject.getFinish_date());
                selfAssessment.setId(receivedObject.getId());
                Optional<Participant> participant = participantRepository.findById(receivedObject.getParticipant_id());
                selfAssessment.setParticipant(participant.get());
                selfAssessment.setStartDate(receivedObject.getStart_date());

                selfAssessmentRepository.save(selfAssessment);
                eventStoreHandler("self_assessment", "SELF_ASSESSMENT_ADDED", selfAssessment, selfAssessment.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                selfAssessmentRepository.findById(receivedObject.getId()).ifPresent(sa -> {
                    sa.setFinishDate(receivedObject.getFinish_date());
                    Optional<Participant> participant = participantRepository
                            .findById(receivedObject.getParticipant_id());
                    sa.setParticipant(participant.get());
                    sa.setStartDate(receivedObject.getStart_date());

                    selfAssessmentRepository.save(sa);
                    eventStoreHandler("self_assessment", "SELF_ASSESSMENT_UPDATE", sa, sa.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "self_assessment_grade_topic", groupId = groupId)
    public void consumeSelfAssessmentGrade(String message) {
        LOGGER.info(String.format("Message Self Assessment received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SelfAssessmentGradeKafka receivedObject = objectMapper.readValue(message, SelfAssessmentGradeKafka.class);

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SelfAssessmentGrade grade = new SelfAssessmentGrade();

                Optional<SelfAssessmentAspect> selfAssessmentAspect = selfAssessmentAspectRepository
                        .findById(receivedObject.getCriteriaSelfAssessmentId());
                grade.setSelfAssessmentAspect(selfAssessmentAspect.get());
                grade.setId(receivedObject.getId());
                Optional<SelfAssessment> selfAssessment = selfAssessmentRepository
                        .findById(receivedObject.getSelfAssessmentId());
                grade.setSelfAssessment(selfAssessment.get());
                grade.setValueSelfAssessment(receivedObject.getGradeSelfAssessment());

                selfAssessmentGradeRepository.save(grade);
                eventStoreHandler("self_assessment_grade", "SELF_ASSESSMENT_GRADE_ADDED", grade, grade.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {

                selfAssessmentGradeRepository.findById(receivedObject.getId()).ifPresent(sa -> {
                    Optional<SelfAssessmentAspect> selfAssessmentAspect = selfAssessmentAspectRepository
                            .findById(receivedObject.getCriteriaSelfAssessmentId());
                    sa.setSelfAssessmentAspect(selfAssessmentAspect.get());
                    Optional<SelfAssessment> selfAssessment = selfAssessmentRepository
                            .findById(receivedObject.getSelfAssessmentId());
                    sa.setSelfAssessment(selfAssessment.get());
                    sa.setValueSelfAssessment(receivedObject.getGradeSelfAssessment());

                    selfAssessmentGradeRepository.save(sa);
                    eventStoreHandler("self_assessment_grade", "SELF_ASSESSMENT_GRADE_UPDATE", sa, sa.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "self_assessment_aspect_topic", groupId = groupId)
    public void consumeSelfAssessmentAspect(String message) {
        LOGGER.info(String.format("Message Self Assessment received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SelfAssessmentAspectKafka receivedObject = objectMapper.readValue(message, SelfAssessmentAspectKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SelfAssessmentAspect aspect = new SelfAssessmentAspect();

                aspect.setDescription(receivedObject.getDescription());
                aspect.setId(receivedObject.getId());
                aspect.setName(receivedObject.getName());
                aspect.setStatus(receivedObject.getStatus());

                selfAssessmentAspectRepository.save(aspect);
                eventStoreHandler("self_assessment_aspect", "SELF_ASSESSMENT_ASPECT_ADDED", aspect, aspect.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                selfAssessmentAspectRepository.findById(receivedObject.getId()).ifPresent(sa -> {
                    sa.setDescription(receivedObject.getDescription());
                    sa.setName(receivedObject.getName());
                    sa.setStatus(receivedObject.getStatus());

                    selfAssessmentAspectRepository.save(sa);
                    eventStoreHandler("self_assessment_aspect", "SELF_ASSESSMENT_ASPECT_UPDATE", sa, sa.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "supervisor_topic", groupId = groupId)
    public void consumeSupervisorGrade(String message) {
        LOGGER.info(String.format("Message Supervisor received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SupervisorGradeKafka receivedObject = objectMapper.readValue(message, SupervisorGradeKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SupervisorGrade supervisor = new SupervisorGrade();

                supervisor.setDate(receivedObject.getDate());
                supervisor.setId(receivedObject.getId());
                Optional<Participant> participant = participantRepository.findById(receivedObject.getParticipantId());
                supervisor.setParticipant(participant.get());
                supervisor.setPhase(receivedObject.getPhase());
                Optional<SupervisorMapping> supervisorMapping = supervisorMappingRepository
                        .findById(receivedObject.getSupervisorGradeId());
                supervisor.setSupervisor(supervisorMapping.get());

                supervisorGradeRepository.save(supervisor);
                eventStoreHandler("supervisor_grade", "SUPERVISOR_GRADE_ADDED", supervisor, supervisor.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                supervisorGradeRepository.findById(receivedObject.getId()).ifPresent(sa -> {
                    sa.setDate(receivedObject.getDate());
                    sa.setId(receivedObject.getId());
                    Optional<Participant> participant = participantRepository
                            .findById(receivedObject.getParticipantId());
                    sa.setParticipant(participant.get());
                    sa.setPhase(receivedObject.getPhase());
                    Optional<SupervisorMapping> supervisorMapping = supervisorMappingRepository
                            .findById(receivedObject.getSupervisorGradeId());
                    sa.setSupervisor(supervisorMapping.get());

                    supervisorGradeRepository.save(sa);
                    eventStoreHandler("supervisor_grade", "SUPERVISOR_GRADE_UPDATE", sa, sa.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "supervisor_result_topic", groupId = groupId)
    public void consumeSupervisorGradeResult(String message) {
        LOGGER.info(String.format("Message Supervisor Result received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SupervisorGradeResultKafka receivedObject = objectMapper.readValue(message,
                    SupervisorGradeResultKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SupervisorGradeResult result = new SupervisorGradeResult();

                result.setId(receivedObject.getId());
                Optional<SupervisorGradeAspect> supervisorGradeAspect = supervisorGradeAspectRepository
                        .findById(receivedObject.getAspectId());
                result.setSupervisorGradeAspect(supervisorGradeAspect.get());
                Optional<SupervisorGrade> supervisorGrade = supervisorGradeRepository
                        .findById(receivedObject.getSupervisorGradeId());
                result.setSupervisorGrade(supervisorGrade.get());
                result.setValue(receivedObject.getValue());

                supervisorGradeResultRepository.save(result);
                eventStoreHandler("supervisor_grade_result", "SUPERVISOR_GRADE_RESULT_ADDED", result, result.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                supervisorGradeResultRepository.findById(receivedObject.getId()).ifPresent(r -> {
                    Optional<SupervisorGradeAspect> supervisorGradeAspect = supervisorGradeAspectRepository
                            .findById(receivedObject.getAspectId());
                    r.setSupervisorGradeAspect(supervisorGradeAspect.get());
                    Optional<SupervisorGrade> supervisorGrade = supervisorGradeRepository
                            .findById(receivedObject.getSupervisorGradeId());
                    r.setSupervisorGrade(supervisorGrade.get());
                    r.setValue(receivedObject.getValue());

                    supervisorGradeResultRepository.save(r);
                    eventStoreHandler("supervisor_grade_result", "SUPERVISOR_GRADE_RESULT_UPDATE", r, r.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "supervisor_aspect_topic", groupId = groupId)
    public void consumeSupervisorGradeAspect(String message) {
        LOGGER.info(String.format("Message Supervisor Aspect received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SupervisorGradeAspectKafka receivedObject = objectMapper.readValue(message,
                    SupervisorGradeAspectKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SupervisorGradeAspect aspect = new SupervisorGradeAspect();

                aspect.setName(receivedObject.getName());
                aspect.setProdiId(receivedObject.getProdiId());
                aspect.setStatus(receivedObject.getStatus());
                aspect.setGradeWeight((float)receivedObject.getMaxGrade());
                aspect.setId(receivedObject.getId());

                supervisorGradeAspectRepository.save(aspect);
                eventStoreHandler("supervisor_grade_aspect", "SUPERVISOR_GRADE_ASPECT_ADDED", aspect, aspect.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                supervisorGradeAspectRepository.findById(receivedObject.getId()).ifPresent(as -> {
                    as.setName(receivedObject.getName());
                    as.setProdiId(receivedObject.getProdiId());
                    as.setStatus(receivedObject.getStatus());
                    as.setGradeWeight((float)receivedObject.getMaxGrade());

                    supervisorGradeAspectRepository.save(as);
                    eventStoreHandler("supervisor_grade_aspect", "SUPERVISOR_GRADE_ASPECT_UPDATE", as, as.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "supervisor_mapping_topic", groupId = groupId)
    public void consumeSupervisorMapping(String message) {
        LOGGER.info(String.format("Message Supervisor Aspect received -> %s", message));
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            SupervisorMappingKafka receivedObject = objectMapper.readValue(message, SupervisorMappingKafka.class);

            // Lakukan operasi apa pun pada objek yang diterima
            System.out.println("==============-----------------------------==============");
            System.out.println("ID: " + receivedObject.getId());
            System.out.println("Data: " + receivedObject.toString());
            System.out.println("==============-----------------------------==============");

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                SupervisorMapping mapping = new SupervisorMapping();

                mapping.setId(receivedObject.getId());
                Optional<Company> company = companyRepository.findById(receivedObject.getCompanyMappingId());
                mapping.setCompany(company.get());
                mapping.setMappingDate(receivedObject.getMappingDate());
                Optional<Participant> participant = participantRepository
                        .findById(receivedObject.getParticipantMappingId());
                mapping.setParticipant(participant.get());
                mapping.setProdiId(receivedObject.getProdiId());
                Optional<Lecturer> lecturer = lecturerRepository.findById(receivedObject.getLecturerId());
                mapping.setLecturer(lecturer.get());

                supervisorMappingRepository.save(mapping);
                eventStoreHandler("supervisor_mapping", "SUPERVISOR_MAPPING_ADDED", mapping, mapping.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                supervisorMappingRepository.findById(receivedObject.getId()).ifPresent(m -> {
                    Optional<Company> company = companyRepository.findById(receivedObject.getCompanyMappingId());
                    m.setCompany(company.get());
                    m.setMappingDate(receivedObject.getMappingDate());
                    Optional<Participant> participant = participantRepository
                            .findById(receivedObject.getParticipantMappingId());
                    m.setParticipant(participant.get());
                    m.setProdiId(receivedObject.getProdiId());
                    Optional<Lecturer> lecturer = lecturerRepository.findById(receivedObject.getLecturerId());
                    m.setLecturer(lecturer.get());

                    supervisorMappingRepository.save(m);
                    eventStoreHandler("supervisor_mapping", "SUPERVISOR_MAPPING_UPDATE", m, m.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "lecturer_topic", groupId = groupId)
    public void consumeLecturer(String message) {
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            LecturerKafka receivedObject = objectMapper.readValue(message, LecturerKafka.class);

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Lecturer lecturer = new Lecturer();

                lecturer.setId(receivedObject.getId());
                lecturer.setName(receivedObject.getName());
                lecturer.setProdiId(receivedObject.getProdiId());

                Optional<Account> account = accountRepository.findById(receivedObject.getAccount_id());
                lecturer.setAccount(account.get());

                lecturerRepository.save(lecturer);
                eventStoreHandler("lecturer", "LECTURER_ADDED", lecturer, lecturer.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                lecturerRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    l.setName(receivedObject.getName());
                    l.setProdiId(receivedObject.getProdiId());

                    lecturerRepository.save(l);
                    eventStoreHandler("lecturer", "LECTURER_UPDATE", l, l.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                lecturerRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    lecturerRepository.delete(l);
                    eventStoreHandler("lecturer", "LECTURER_DELETE", l, l.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "deadline_topic", groupId = groupId)
    public void consumeDeadline(String message) {
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            DeadlineKafka receivedObject = objectMapper.readValue(message, DeadlineKafka.class);

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Deadline deadline = new Deadline();

                deadline.setDayRange(receivedObject.getDayRange());
                deadline.setFinishDate(receivedObject.getFinishDate());
                deadline.setId(receivedObject.getId());
                deadline.setName(receivedObject.getName());
                deadline.setStartDate(receivedObject.getStartDate());

                deadlineRepository.save(deadline);
                eventStoreHandler("deadline", "DEADLINE_ADDED", deadline, deadline.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                deadlineRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    l.setDayRange(receivedObject.getDayRange());
                    l.setFinishDate(receivedObject.getFinishDate());
                    l.setName(receivedObject.getName());
                    l.setStartDate(receivedObject.getStartDate());

                    deadlineRepository.save(l);
                    eventStoreHandler("deadline", "DEADLINE_UPDATE", l, l.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                deadlineRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    deadlineRepository.delete(l);
                    eventStoreHandler("deadline", "DEADLINE_DELETE", l, l.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "prerequisite_topic", groupId = groupId)
    public void consumePrerequisite(String message) {
        try {
            // Mengubah string JSON menjadi objek
            ObjectMapper objectMapper = new ObjectMapper();
            PrerequisiteKafka receivedObject = objectMapper.readValue(message, PrerequisiteKafka.class);

            // proses melakukan save pada tabel account
            if (receivedObject.getOperation().equalsIgnoreCase("ADDED")) {
                Prerequisite prerequisite = new Prerequisite();

                prerequisite.setId(receivedObject.getId());
                prerequisite.setYear(receivedObject.getYear());
                Optional<Company> company = companyRepository.findById(receivedObject.getCompany_id());
                prerequisite.setCompany(company.get());

                prerequisiteRepository.save(prerequisite);
                eventStoreHandler("prerequisite", "PREREQUISITE_ADDED", prerequisite, prerequisite.getId());
            } else if (receivedObject.getOperation().equalsIgnoreCase("UPDATE")) {
                prerequisiteRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    l.setId(receivedObject.getId());
                    l.setYear(receivedObject.getYear());
                    Optional<Company> company = companyRepository.findById(receivedObject.getCompany_id());
                    l.setCompany(company.get());

                    prerequisiteRepository.save(l);
                    eventStoreHandler("prerequisite", "PREREQUISITE_UPDATE", l, l.getId());
                });
            } else if (receivedObject.getOperation().equalsIgnoreCase("DELETE")) {
                prerequisiteRepository.findById(receivedObject.getId()).ifPresent(l -> {

                    prerequisiteRepository.delete(l);
                    eventStoreHandler("prerequisite", "PREREQUISITE_DELETE", l, l.getId());
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
