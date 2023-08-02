package com.jtk.ps.api.service;

import java.io.ByteArrayInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtk.ps.api.dto.ComponentAndCriteriasDto;
import com.jtk.ps.api.dto.ComponentCourseDto;
import com.jtk.ps.api.dto.CourseFormRequestDto;
import com.jtk.ps.api.dto.CourseFormResponseDto;
import com.jtk.ps.api.dto.CriteriaBodyDto;
import com.jtk.ps.api.dto.CriteriaEvaluationFormDto;
import com.jtk.ps.api.dto.EvaluationFormResponseDto;
import com.jtk.ps.api.dto.EvaluationIndustryDto;
import com.jtk.ps.api.dto.EventStoreDto;
import com.jtk.ps.api.dto.IsFinalizationDto;
import com.jtk.ps.api.dto.RecapitulationComponentDto;
import com.jtk.ps.api.dto.RecapitulationCourseDto;
import com.jtk.ps.api.dto.RecapitulationCriteriaDto;
import com.jtk.ps.api.dto.RecapitulationParticipantDto;
import com.jtk.ps.api.dto.TypeOfAspectEvaluationDto;
import com.jtk.ps.api.helper.ExcelHelper;
import com.jtk.ps.api.model.AssessmentAspect;
import com.jtk.ps.api.model.ComponentCourse;
import com.jtk.ps.api.model.CourseForm;
import com.jtk.ps.api.model.CourseValues;
import com.jtk.ps.api.model.CriteriaComponentCourse;
import com.jtk.ps.api.model.Evaluation;
import com.jtk.ps.api.model.EvaluationForm;
import com.jtk.ps.api.model.EventStore;
import com.jtk.ps.api.model.Participant;
import com.jtk.ps.api.model.SelfAssessmentAspect;
import com.jtk.ps.api.model.SelfAssessmentGrade;
import com.jtk.ps.api.model.SeminarCriteria;
import com.jtk.ps.api.model.SeminarValues;
import com.jtk.ps.api.model.SupervisorGradeAspect;
import com.jtk.ps.api.model.SupervisorGradeResult;
import com.jtk.ps.api.model.Timeline;
import com.jtk.ps.api.model.TotalComponents;
import com.jtk.ps.api.model.TotalCourses;
import com.jtk.ps.api.model.Valuation;
import com.jtk.ps.api.repository.AccountRepository;
import com.jtk.ps.api.repository.AssessmentAspectRepository;
import com.jtk.ps.api.repository.CompanyRepository;
import com.jtk.ps.api.repository.ComponentCourseRepository;
import com.jtk.ps.api.repository.CourseFormRepository;
import com.jtk.ps.api.repository.CourseValuesRepository;
import com.jtk.ps.api.repository.CriteriaComponentCourseRepository;
import com.jtk.ps.api.repository.EvaluationFormRepository;
import com.jtk.ps.api.repository.EvaluationRepository;
import com.jtk.ps.api.repository.EventStoreRepository;
import com.jtk.ps.api.repository.ParticipantRepository;
import com.jtk.ps.api.repository.SelfAssessmentAspectRepository;
import com.jtk.ps.api.repository.SelfAssessmentGradeRepository;
import com.jtk.ps.api.repository.SeminarCriteriaRepository;
import com.jtk.ps.api.repository.SeminarValuesRepository;
import com.jtk.ps.api.repository.SupervisorGradeAspectRepository;
import com.jtk.ps.api.repository.SupervisorGradeResultRepository;
import com.jtk.ps.api.repository.TimelineRepository;
import com.jtk.ps.api.repository.TotalComponentsRepository;
import com.jtk.ps.api.repository.TotalCoursesRepository;
import com.jtk.ps.api.repository.ValuationRepository;
import com.jtk.ps.api.service.Interface.ICourseService;

@Service
public class CourseService implements ICourseService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    @Lazy
    private CourseFormRepository courseFormRepository;

    @Autowired
    @Lazy
    private EventStoreRepository eventStoreRepository;

    @Autowired
    @Lazy
    private ComponentCourseRepository componentCourseRepository;

    @Autowired
    @Lazy
    private EvaluationFormRepository evaluationFormRepository;

    @Autowired
    @Lazy
    private AssessmentAspectRepository assessmentAspectRepository;

    @Autowired
    @Lazy
    private SelfAssessmentAspectRepository selfAssessmentAspectRepository;

    @Autowired
    @Lazy
    private SupervisorGradeAspectRepository supervisorGradeAspectRepository;

    @Autowired
    @Lazy
    private SeminarCriteriaRepository seminarCriteriaRepository;

    @Autowired
    @Lazy
    private CriteriaComponentCourseRepository criteriaComponentCourseRepository;

    @Autowired
    @Lazy
    private CourseValuesRepository courseValuesRepository;

    @Autowired
    @Lazy
    private ParticipantRepository participantRepository;

    @Autowired
    @Lazy
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private EvaluationRepository evaluationRepository;

    @Autowired
    @Lazy
    private SelfAssessmentGradeRepository selfAssessmentGradeRepository;

    @Autowired
    @Lazy
    private SupervisorGradeResultRepository supervisorGradeResultRepository;

    @Autowired
    @Lazy
    private ValuationRepository valuationRepository;

    @Autowired
    @Lazy
    private SeminarValuesRepository seminarValuesRepository;

    @Autowired
    @Lazy
    private TimelineRepository timelineRepository;

    @Autowired
    @Lazy
    private TotalComponentsRepository totalComponentsRepository;

    @Autowired
    @Lazy
    private TotalCoursesRepository totalCoursesRepository;

    @Autowired
    @Lazy
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void eventStoreHandler(String entityId, String eventType, Object object,Integer eventDataId){
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

    @Override
    public List<CourseFormResponseDto> getAllCourse(Integer roleId,Integer prodiId) {
        List<CourseForm> courseForms = courseFormRepository.findAllCourse(Integer.parseInt(Year.now().toString()));

        List<CourseFormResponseDto> courseFormResponseDtos = new ArrayList<>();
        courseForms.forEach(c -> {
            if(!(roleId == 0 && c.getIsFinalization() == 1) && prodiId == c.getProdiId()){   
                CourseFormResponseDto temp = new CourseFormResponseDto();
    
                temp.setId(c.getId());
                temp.setKode(c.getKode());
                temp.setProdiId(c.getProdiId());
                temp.setSks(c.getSks());
                temp.setTahunAjaranEnd(c.getTahunAjaranEnd());
                temp.setTahunAjaranStart(c.getTahunAjaranStart());
                temp.setName(c.getName());
    
                courseFormResponseDtos.add(temp);
            }
        });
        return courseFormResponseDtos;
    }

    private void create6ComponentCourses(Integer idForm){
        String[] component = {
            "EAS Praktek", "EAS Teori", "ETS Praktek", "ETS Teori", "Lain-lain Praktek", "Lain-lain Teori"
        };

        for (int i = 0; i < 6; i++) {
            ComponentCourse componentCourse = new ComponentCourse();
            
            componentCourse.setBobotComponent(0);
            Optional<CourseForm> course = courseFormRepository.findById(idForm);
            componentCourse.setCourse(course.get());
            componentCourse.setIsAverage(1);
            componentCourse.setName(component[i]);

            componentCourse = componentCourseRepository.save(componentCourse);
            eventStoreHandler("component_course", "COMPONENT_COURSE_ADDED", componentCourse, componentCourse.getId());
        }
    }

    @Override
    public CourseForm createCourseForm(CourseFormRequestDto courseFormRequestDto) {
        
        CourseForm courseForm = new CourseForm();

        courseForm.setKode(courseFormRequestDto.getKode());
        courseForm.setName(courseFormRequestDto.getName());
        courseForm.setProdiId(courseFormRequestDto.getProdiId());
        courseForm.setSks(courseFormRequestDto.getSks());
        courseForm.setTahunAjaranEnd(courseFormRequestDto.getTahunAjaranEnd());
        courseForm.setTahunAjaranStart(courseFormRequestDto.getTahunAjaranStart());
        courseForm.setIsDeleted(0);
        courseForm.setIsFinalization(0);

        courseForm = courseFormRepository.save(courseForm);

        // membuat komponen
        create6ComponentCourses(courseForm.getId());

        // mencatat event
        eventStoreHandler("course_form", "COURSE_FORM_ADDED", courseForm, courseForm.getId());

        return courseForm;
    }

    @Override
    public CourseFormResponseDto getDetailCourse(Integer idForm) {

        CourseFormResponseDto courseFormResponseDto = new CourseFormResponseDto();

        Optional<CourseForm> cOptional = courseFormRepository.findById(idForm);

        cOptional.ifPresent(c -> {
            courseFormResponseDto.setId(c.getId());
            courseFormResponseDto.setKode(c.getKode());
            courseFormResponseDto.setName(c.getName());
            courseFormResponseDto.setProdiId(c.getProdiId());
            courseFormResponseDto.setSks(c.getSks());
            courseFormResponseDto.setTahunAjaranEnd(c.getTahunAjaranEnd());
            courseFormResponseDto.setTahunAjaranStart(c.getTahunAjaranStart());
        });
        
        return courseFormResponseDto;
    }

    @Override
    public void deleteCourseForm(Integer idForm) {
        Optional<CourseForm> courseForm = courseFormRepository.findById(idForm);

        courseForm.ifPresent(c -> {
            c.setIsDeleted(1);

            eventStoreHandler("course_form", "COURSE_FORM_UPDATE", courseFormRepository.save(c), c.getId());
        });
    }

    @Override
    public void updateCourseForm(Integer idForm, CourseFormRequestDto courseFormRequestDto) {
        
        Optional<CourseForm> newCourseForm = courseFormRepository.findById(idForm);

        newCourseForm.ifPresent(c -> {
            c.setKode(courseFormRequestDto.getKode());
            c.setName(courseFormRequestDto.getName());
            c.setProdiId(courseFormRequestDto.getProdiId());
            c.setSks(courseFormRequestDto.getSks());
            c.setTahunAjaranEnd(courseFormRequestDto.getTahunAjaranEnd());
            c.setTahunAjaranStart(courseFormRequestDto.getTahunAjaranStart());

            eventStoreHandler("course_form", "COURSE_FORM_UPDATE", courseFormRepository.save(c), c.getId());
        });
    }

    @Override
    public List<EvaluationFormResponseDto> getEvaluationForm(Integer prodiId) {
        
        String[] formNames = {
            "Industri", "Pembimbing", "Self Assessment", "Seminar"
        };

        List<EvaluationFormResponseDto> evaluationForms = new ArrayList<>();

        for (int i = 0; i < formNames.length; i++) {
                EvaluationFormResponseDto temp = new EvaluationFormResponseDto();

                temp.setFormType(formNames[i]);
                temp.setFormName(formNames[i]);

                evaluationForms.add(temp);
        }

        return evaluationForms;
    }

    @Override
    public List<CriteriaEvaluationFormDto> getCriteriaByEvaluationForm(String formType, String formName, Integer prodiId) {

        List<CriteriaEvaluationFormDto> criteriaEvaluationForms = new ArrayList<>();
        String[] numEvaluation = formType.split(" ");
        String decision = "";

        if(formName.equalsIgnoreCase("Industri")){
            decision = formType;
        }else{
            decision = formName;
        }

        criteriaEvaluationForms.add( new CriteriaEvaluationFormDto(0,"Semua aspek"));
        switch (decision) {
            case "Pembimbing":
                List<SupervisorGradeAspect> pembimbingAspects = supervisorGradeAspectRepository.findAll();

                pembimbingAspects.forEach(p -> {
                    CriteriaEvaluationFormDto temp = new CriteriaEvaluationFormDto();

                    temp.setId(p.getId());
                    temp.setName(p.getDescription());

                    criteriaEvaluationForms.add(temp);
                });
                break;
            case "Self Assessment":
                List<SelfAssessmentAspect> selfAssessmentAspects = selfAssessmentAspectRepository.findAll();

                selfAssessmentAspects.forEach(s -> {
                    CriteriaEvaluationFormDto temp = new CriteriaEvaluationFormDto();

                    temp.setId(s.getId());
                    temp.setName(s.getName());

                    criteriaEvaluationForms.add(temp);
                });
                break;
            case "Seminar":
                List<SeminarCriteria> seminarCriterias = seminarCriteriaRepository.findAllBySelected();

                seminarCriterias.forEach(s -> {
                    CriteriaEvaluationFormDto temp = new CriteriaEvaluationFormDto();

                    temp.setId(s.getId());
                    temp.setName(s.getCriteriaName());

                    criteriaEvaluationForms.add(temp);
                });
                break;
        
            default:
                LOGGER.info(String.format("******* value fo numEvaluation %s", numEvaluation[1]));
                List<AssessmentAspect> industriAspects = assessmentAspectRepository.findAllByNumEvaluation(Integer.parseInt(numEvaluation[1]),prodiId);

                industriAspects.forEach(i -> {
                    CriteriaEvaluationFormDto temp = new CriteriaEvaluationFormDto();

                    temp.setId(i.getId());
                    temp.setName(i.getAspectName());

                    criteriaEvaluationForms.add(temp);
                });
                break;
        }

        return criteriaEvaluationForms;
    }

    @Override
    public List<TypeOfAspectEvaluationDto> getTypeAspectEvaluationForm(String formType,Integer prodiId){

        String prodiName = "";
        List<TypeOfAspectEvaluationDto> listTypes = new ArrayList<>();

        if(prodiId == 0){
            prodiName = "KP";
        }else if(prodiId == 1){
            prodiName = "PKL";
        }

        if(formType.equalsIgnoreCase("Self Assessment")){
            Integer numbWeeks = timelineRepository.countWeekInSelfAssessment("%Pelaksanaan "+ prodiName+"%");

            for(int i = 1; i <= numbWeeks; i++){
                TypeOfAspectEvaluationDto type = new TypeOfAspectEvaluationDto();
                type.setName("Minggu "+i);
                listTypes.add(type);
            }
        }else if(formType.equalsIgnoreCase("Pembimbing")){
            Integer numbPhase = timelineRepository.countPhaseMentor("%Pembimbing "+prodiName+"%");

            for(int i = 1; i <= numbPhase; i++){
                TypeOfAspectEvaluationDto type = new TypeOfAspectEvaluationDto();
                type.setName("Phase "+i);
                listTypes.add(type);
            }
        }else if(formType.equalsIgnoreCase("Seminar")){
            TypeOfAspectEvaluationDto penguji1 = new TypeOfAspectEvaluationDto();
            penguji1.setName("Penguji 1");
            listTypes.add(penguji1);

            TypeOfAspectEvaluationDto penguji2 = new TypeOfAspectEvaluationDto();
            penguji2.setName("Penguji 2");
            listTypes.add(penguji2);

            TypeOfAspectEvaluationDto penguji3 = new TypeOfAspectEvaluationDto();
            penguji3.setName("Pembimbing");
            listTypes.add(penguji3);
        }else if(formType.equalsIgnoreCase("Industri")){
            List<EvaluationForm> industriForms = evaluationFormRepository.findAllByProdiId(prodiId);
            industriForms.forEach(c -> {
                TypeOfAspectEvaluationDto type = new TypeOfAspectEvaluationDto();
                type.setName("Evaluasi "+String.valueOf(c.getNumEvaluation()));
                listTypes.add(type);
            });
        }
        
        return listTypes;
    }
	@Override
	public List<ComponentCourseDto> getComponentByCourseForm(Integer idForm) {
		
        List<ComponentCourseDto> response = new ArrayList<>();

        List<ComponentCourse> componentCourses = componentCourseRepository.findAllByFormId(idForm);

        componentCourses.forEach(c -> {
            ComponentCourseDto temp = new ComponentCourseDto();
            
            temp.setBobotComponent(c.getBobotComponent());
            temp.setCourseId(c.getCourse().getId());
            temp.setId(c.getId());
            temp.setIsAverage(c.getIsAverage());
            temp.setName(c.getName());

            response.add(temp);
        });
		return response;
	}

	@Override
	public void updateComponent(List<ComponentCourseDto> componentCourseDtos) {
		componentCourseDtos.forEach(c -> {
            Optional<ComponentCourse> componentCourse = componentCourseRepository.findById(c.getId());

            componentCourse.ifPresent(p -> {
                p.setBobotComponent(c.getBobotComponent());
                Optional<CourseForm> course = courseFormRepository.findById(c.getCourseId());
                p.setCourse(course.get());
                p.setIsAverage(c.getIsAverage());
                p.setName(c.getName());

                eventStoreHandler("component_course", "COMPONENT_COURSE_UPDATE", componentCourseRepository.save(p), p.getId());
            });
        });
	}

	@Override
    public List<ComponentAndCriteriasDto> getCriteriaComponentByCourseFormId(Integer idForm) {
        
        List<ComponentAndCriteriasDto> response = new ArrayList<>();

        List<ComponentCourse> componentCourses = componentCourseRepository.findAllByFormId(idForm);
        

        componentCourses.forEach(c -> {
            ComponentAndCriteriasDto temp = new ComponentAndCriteriasDto();
            
            temp.setId(c.getId());
            temp.setIsAverage(c.getIsAverage());
            temp.setName(c.getName());

            List<CriteriaComponentCourse> criteriaComponentCourses = criteriaComponentCourseRepository.findAllByComponentId(c.getId());

            List<CriteriaBodyDto> criteriaForResponses = new ArrayList<>();

            // mengambil data criteria pada komponen tertentu
            criteriaComponentCourses.forEach(p -> {
                CriteriaBodyDto criteriaTemp = new CriteriaBodyDto();

                criteriaTemp.setComponentId(p.getComponentCourse().getId());
                criteriaTemp.setId(p.getId());
                criteriaTemp.setNameForm(p.getNameForm());
                criteriaTemp.setTypeForm(p.getTypeForm());
                criteriaTemp.setBobotCriteria(p.getBobotCriteria());

                if(p.getIndustryCriteria() != null){
                    criteriaTemp.setAspectFormId(p.getIndustryCriteria().getId());
                    criteriaTemp.setAspectName(criteriaComponentCourseRepository.getNameAspectFromIndustry(p.getIndustryCriteria().getId()));
                }else if(p.getSelfAssessmentCriteria() != null){
                    criteriaTemp.setAspectFormId(p.getSelfAssessmentCriteria().getId());
                    criteriaTemp.setAspectName(criteriaComponentCourseRepository.getNameAspectFromSelfAssessment(p.getSelfAssessmentCriteria().getId()));
                }else if(p.getSeminarCriteria() != null){
                    criteriaTemp.setAspectFormId(p.getSeminarCriteria().getId());
                    criteriaTemp.setAspectName(criteriaComponentCourseRepository.getNameAspectFromSeminar(p.getSeminarCriteria().getId()));
                }else if(p.getSupervisorCriteria() != null){
                    criteriaTemp.setAspectFormId(p.getSupervisorCriteria().getId());
                    criteriaTemp.setAspectName(criteriaComponentCourseRepository.getNameAspectFromSupervisor(p.getSupervisorCriteria().getId()));
                }

                criteriaForResponses.add(criteriaTemp);
            });

            temp.setCriteria_data(criteriaForResponses);

            response.add(temp);
        });

        return response;
    }

    private List<CriteriaBodyDto> removeDuplicateCriteria(List<CriteriaBodyDto> newCriterias){
        List<CriteriaBodyDto> tempCriterias = new ArrayList<>();
        Set<Integer> uniqueAspectIndustri = new HashSet<>();
        Set<Integer> uniqueAspectSA = new HashSet<>();
        Set<Integer> uniqueAspectSupervisor = new HashSet<>();
        Set<Integer> uniqueAspectSeminar = new HashSet<>();

        newCriterias.forEach(n -> {
            boolean isSame = false;
            switch(n.getNameForm()){
                    case "Industri":
                        isSame = uniqueAspectIndustri.add(n.getAspectFormId());
                        break;
                    case "Seminar":
                        isSame = uniqueAspectSeminar.add(n.getAspectFormId());
                        break;
                    case "Pembimbing":
                        isSame = uniqueAspectSupervisor.add(n.getAspectFormId());
                        break;
                    case "Self Assessment":
                        isSame = uniqueAspectSA.add(n.getAspectFormId());
                        break;
                }
            System.out.println("isSame ==>"+isSame);
            System.out.println();
            if ((n.getAspectFormId() != 0 && !n.getAspectName().equals("Semua aspek")) ) {
                if(isSame == true){
                    tempCriterias.add(n);
                }
            }
        });
        return tempCriterias;
        
    }
    
    @Override
	public void updateOrInsertCriteriaComponent(ComponentAndCriteriasDto newCriterias, Integer prodiId) {

        List<CriteriaComponentCourse> oldCriterias = criteriaComponentCourseRepository.findAllByComponentId(newCriterias.getId());
        List<CriteriaBodyDto> tempCriteriasDto = new ArrayList<>();

        List<Integer> doneUpdateOrDelete = new ArrayList<>();
        
        for(CriteriaBodyDto c : newCriterias.getCriteria_data()){
            if(c.getAspectName().equals("Semua aspek")){
                for(CriteriaEvaluationFormDto e : getCriteriaByEvaluationForm(c.getTypeForm(),c.getNameForm(),prodiId)){
                    if(e.getId() != 0 && !e.getName().equalsIgnoreCase("Semua Aspek")){
                        CriteriaBodyDto temp = new CriteriaBodyDto();
                        temp.setAspectFormId(e.getId());
                        temp.setAspectName(e.getName());
                        temp.setBobotCriteria(100);
                        temp.setComponentId(newCriterias.getId());
                        temp.setNameForm(c.getNameForm());
                        temp.setTypeForm(c.getTypeForm());
                        tempCriteriasDto.add(temp);
                    }
                }
            }else{
                tempCriteriasDto.add(c);
            }
        };
        
        tempCriteriasDto = removeDuplicateCriteria(tempCriteriasDto);


        for(CriteriaComponentCourse o:oldCriterias){
            Integer isExist = 0;
            
            // mencari tahu apakah criteria ini dihapus atau tidak
            for (int i = 0; i < tempCriteriasDto.size(); i++) {
                CriteriaBodyDto n = tempCriteriasDto.get(i);

                if(o.getId().equals(n.getId())){
                    o.setBobotCriteria(n.getBobotCriteria());
                    o.setNameForm(n.getNameForm());
                    o.setTypeForm(n.getTypeForm());

                    // menghapus fk semua paada kriteria
                    o.setIndustryCriteria(null);
                    o.setSeminarCriteria(null);
                    o.setSupervisorCriteria(null);
                    o.setSelfAssessmentCriteria(null);
                    switch(n.getNameForm()){
                        case "Industri":
                            Optional<AssessmentAspect> assessmentAspect = assessmentAspectRepository.findById(n.getAspectFormId());
                            o.setIndustryCriteria(assessmentAspect.get());
                            break;
                        case "Seminar":
                            Optional<SeminarCriteria> seminarCriteria = seminarCriteriaRepository.findById(n.getAspectFormId());
                            o.setSeminarCriteria(seminarCriteria.get());
                            break;
                        case "Pembimbing":
                            Optional<SupervisorGradeAspect> supervisorAspect = supervisorGradeAspectRepository.findById(n.getAspectFormId());
                            o.setSupervisorCriteria(supervisorAspect.get());
                            break;
                        case "Self Assessment":
                            Optional<SelfAssessmentAspect> selfAssessmentAspect = selfAssessmentAspectRepository.findById(n.getAspectFormId());
                            o.setSelfAssessmentCriteria(selfAssessmentAspect.get());
                            break;
                    }

                    isExist = 1;
                    doneUpdateOrDelete.add(n.getId());
                    eventStoreHandler("criteria_component_course", "CRITERIA_COMPONENT_COURSE_UPDATE",criteriaComponentCourseRepository.save(o), o.getId());
                }
            }
            // jika criteria tidak ada pada newCriteria maka akan dihapus
            // menentukan soft delete atau hard delete
            if(isExist == 0){
                System.out.println(" test  1");
                // LOGGER.info(String.format("year now ==> %d", Integer.valueOf(Year.now().toString())));
                // LOGGER.info(String.format("nilai dari is criteria %d", courseValuesRepository.isCriteriaInYearNowUse(o.getId(), Integer.valueOf(Year.now().toString()))));

                // soft delete
                if(courseValuesRepository.isCriteriaInBeforeYearUse(o.getId(), Integer.valueOf(Year.now().toString())) > 0){
                    o.setIsDeleted(1);
                    // menghapus values pada tahun sekarang dan criteria id tersebut
                    courseValuesRepository.deleteAllInCriteriaIdAndYear(o.getId(), Integer.valueOf(Year.now().toString()));
                    eventStoreHandler("criteria_component_course", "CRITERIA_COMPONENT_COURSE_UPDATE",criteriaComponentCourseRepository.save(o), o.getId());
                }else{ //hard delete
                    
                    courseValuesRepository.deleteAllInCriteriaIdAndYear(o.getId(), Integer.valueOf(Year.now().toString()));
                    
                    criteriaComponentCourseRepository.delete(o);

                    eventStoreHandler("criteria_component_course", "CRITERIA_COMPONENT_COURSE_DELETE",o, o.getId());
                }
            }
        };
        
        // create criteria baru jika masih ada sisah
        tempCriteriasDto.forEach(n -> {
            
            if(doneUpdateOrDelete.contains(n.getId()) == false){
                CriteriaComponentCourse newTemp = new CriteriaComponentCourse();
                
                newTemp.setBobotCriteria(n.getBobotCriteria());
                Optional<ComponentCourse> componentCourse = componentCourseRepository.findById(n.getComponentId());
                newTemp.setComponentCourse(componentCourse.get());
                newTemp.setNameForm(n.getNameForm());
                newTemp.setTypeForm(n.getTypeForm());
                newTemp.setIsDeleted(0);
                
                switch(n.getNameForm()){
                    case "Industri":
                        Optional<AssessmentAspect> assessmentAspect = assessmentAspectRepository.findById(n.getAspectFormId());
                        newTemp.setIndustryCriteria(assessmentAspect.get());
                        break;
                    case "Seminar":
                        Optional<SeminarCriteria> seminarCriteria = seminarCriteriaRepository.findById(n.getAspectFormId());
                        newTemp.setSeminarCriteria(seminarCriteria.get());
                        break;
                    case "Pembimbing":
                        Optional<SupervisorGradeAspect> supervisorAspect = supervisorGradeAspectRepository.findById(n.getAspectFormId());
                        newTemp.setSupervisorCriteria(supervisorAspect.get());
                        break;
                    case "Self Assessment":
                        Optional<SelfAssessmentAspect> selfAssessmentAspect = selfAssessmentAspectRepository.findById(n.getAspectFormId());
                        newTemp.setSelfAssessmentCriteria(selfAssessmentAspect.get());
                        break;
                }
                newTemp = criteriaComponentCourseRepository.save(newTemp);
                eventStoreHandler("criteria_component_course", "CRITERIA_COMPONENT_COURSE_ADDED", newTemp, newTemp.getId());
            }
        });

        // update is average pada component 
        Optional<ComponentCourse> newComponent = componentCourseRepository.findById(newCriterias.getId());

        newComponent.ifPresent(c -> {
            c.setIsAverage(newCriterias.getIsAverage());

            eventStoreHandler("component_course", "COMPONENT_COURSE_UPDATE", componentCourseRepository.save(c), c.getId());
        });
	}

    @Override
    public List<RecapitulationCourseDto> getAllRecapitulationByYearAndProdiId(Integer year, Integer prodiId) {
        // tampilkan untuk tahun sekarang aja dulu
        // cari mata kuliah dengan tahun dan prodiId 
        List<CourseForm> courseForms = new ArrayList<>();

        if(Integer.parseInt(Year.now().toString()) == year){
            courseForms = courseFormRepository.findAllCourseByYearAndProdiId(year, prodiId);
        }else{
            courseForms = courseFormRepository.findAllOldCourseByYearAndProdiId(year, prodiId);
        }

        List<RecapitulationCourseDto> responseCourses = new ArrayList<>();
        courseForms.forEach(f -> {
            
            RecapitulationCourseDto tempCourseDtos = new RecapitulationCourseDto();

            tempCourseDtos.setIdCourse(f.getId());
            tempCourseDtos.setNameCourse(f.getName());
            tempCourseDtos.setKode(f.getKode());
            // sekarang mencari peserta yang ada pada mata kuliah tersebut
            // mencari peserta pada year dan prodiId
            
            tempCourseDtos.setParticipant_data(
                getListParticipantForRecapitulation(year, prodiId,f)
            );
            responseCourses.add(tempCourseDtos);
        });

        return responseCourses;
    }

    private List<RecapitulationParticipantDto> getListParticipantForRecapitulation(Integer year, Integer prodiId,CourseForm form){
        List<RecapitulationParticipantDto> responseParticipantDtos = new ArrayList<>();

        List<Participant> participants = participantRepository.findAllByYearAndProdi(year, prodiId);

        participants.forEach(p -> {
            
            RecapitulationParticipantDto tempParticipantDto = new RecapitulationParticipantDto();

            tempParticipantDto.setIdParticipant(p.getId());
            tempParticipantDto.setName(p.getName());
            tempParticipantDto.setNim(p.getAccount().getUsername());

            // ***************************** Component *****************************
            tempParticipantDto.setComponent_data(
                getListComponentRecapitulation(year, prodiId, form, p)
            );
            tempParticipantDto.setTotal_course(0f);
            // setelah menghitung semua kriterianya
            if(form.getIsFinalization() != 0){
                Optional<TotalCourses> oldTotalCourses = totalCoursesRepository.findByCourseIdAndParticipantId(form.getId(), p.getId());
                if(oldTotalCourses.isPresent()){
                    oldTotalCourses.get().setValue(
                        getTotalCourse(tempParticipantDto.getComponent_data())
                    );
                    totalCoursesRepository.save(oldTotalCourses.get());
                }else{
                    TotalCourses newTotalCourses = new TotalCourses();
                    Optional<CourseForm> course = courseFormRepository.findById(form.getId());
                    newTotalCourses.setCourseForm(course.get());
                    Optional<Participant> participant = participantRepository.findById(p.getId());
                    newTotalCourses.setParticipant(participant.get());
                    newTotalCourses.setValue(
                        getTotalCourse(tempParticipantDto.getComponent_data())
                    );
                    totalCoursesRepository.save(newTotalCourses);
                }
                tempParticipantDto.setTotal_course(
                    getTotalCourse(tempParticipantDto.getComponent_data())
                );
            }else{
                totalCoursesRepository.findByCourseIdAndParticipantId(form.getId(), p.getId()).ifPresent(t -> {
                    tempParticipantDto.setTotal_course(
                        t.getValue()
                    );
                });
            }
            responseParticipantDtos.add(tempParticipantDto);
        });
        return responseParticipantDtos;
    }

    private Float getTotalCourse(List<RecapitulationComponentDto> courses){
        float totalCourse = 0f;

        for(RecapitulationComponentDto c : courses){
            if(c.getBobotComponent() != 0){
                totalCourse += (c.getTotalValueComponent() / 100) * c.getBobotComponent();
            }
        }
        return totalCourse;
    }

    private List<RecapitulationComponentDto> getListComponentRecapitulation(Integer year, Integer prodiId,CourseForm form,Participant participant){
        
        List<RecapitulationComponentDto> tempRecapitulationComponentDtos = new ArrayList<>();
        List<ComponentCourse> componentCourses = componentCourseRepository.findAllByFormId(form.getId());

        componentCourses.forEach(c -> {
                
            RecapitulationComponentDto tempRecapitulationComponentDto = new RecapitulationComponentDto();
            int isAverage = c.getIsAverage();
            tempRecapitulationComponentDto.setIdComponent(c.getId());
            tempRecapitulationComponentDto.setNameComponent(c.getName());
            tempRecapitulationComponentDto.setBobotComponent(c.getBobotComponent());

            // ***************************** Criteria and Values *****************************
            tempRecapitulationComponentDto.setCriteria_data(
                getListCriteriaRecapitulation(year, prodiId, c, participant)
            );
            tempRecapitulationComponentDto.setTotalValueComponent(0f);
            // find total 
            if(form.getIsFinalization() != 0){
                Optional<TotalComponents> oldTotalComponents = totalComponentsRepository.findByComponentIdAndParticipantId(c.getId(), participant.getId());
                if(oldTotalComponents.isPresent()){
                    oldTotalComponents.get().setValue(
                        getTotalComponent(isAverage,tempRecapitulationComponentDto.getCriteria_data())
                    );
                    totalComponentsRepository.save(oldTotalComponents.get());
                }else{
                    TotalComponents newTotalComponents = new TotalComponents();
                    Optional<ComponentCourse> componentCourse = componentCourseRepository.findById(c.getId());
                    newTotalComponents.setComponentCourse(componentCourse.get());
                    Optional<Participant> participantOptional = participantRepository.findById(participant.getId());
                    newTotalComponents.setParticipant(participantOptional.get());
                    newTotalComponents.setValue(
                        getTotalComponent(isAverage,tempRecapitulationComponentDto.getCriteria_data())
                    );
                    totalComponentsRepository.save(newTotalComponents);
                }
                tempRecapitulationComponentDto.setTotalValueComponent(getTotalComponent(isAverage,tempRecapitulationComponentDto.getCriteria_data()));
            }else{
                totalComponentsRepository.findByComponentIdAndParticipantId(c.getId(), participant.getId())
                    .ifPresent(t -> {
                        tempRecapitulationComponentDto.setTotalValueComponent(t.getValue());
                    });
            }

            tempRecapitulationComponentDtos.add(tempRecapitulationComponentDto);
        });

        return tempRecapitulationComponentDtos;
    }

    private Float getTotalComponent(Integer isAverage, List<RecapitulationCriteriaDto> listValues){
        float totalComponent = 0f;
        if(listValues.size() == 0){
            return totalComponent;
        }

        if(isAverage == 1){
            for(RecapitulationCriteriaDto v : listValues){
                totalComponent += v.getValue();
            }
            totalComponent = totalComponent / listValues.size();
        }else{
            for(RecapitulationCriteriaDto v : listValues){
                totalComponent += (v.getValue()/100) * v.getBobot();
            }
        }
        return totalComponent;
    }

    private List<RecapitulationCriteriaDto> getListCriteriaRecapitulation(Integer year, Integer prodiId, ComponentCourse component, Participant participant){

        List<RecapitulationCriteriaDto> tCriteriaDtos = new ArrayList<>();

        List<CriteriaComponentCourse> criteriaComponentCourses = new ArrayList<>();
        if(year == Integer.parseInt(Year.now().toString())){
            criteriaComponentCourses = criteriaComponentCourseRepository.findAllByComponentId(component.getId());
        }else{
            criteriaComponentCourses = criteriaComponentCourseRepository.findCriteriaLastYear(year, component.getId());
        }

        criteriaComponentCourses.forEach(d -> {
            RecapitulationCriteriaDto tempRecapitulationCriteriaDto = new RecapitulationCriteriaDto();
            
            tempRecapitulationCriteriaDto.setBobot(d.getBobotCriteria());
            tempRecapitulationCriteriaDto.setNameForm(d.getNameForm());
            tempRecapitulationCriteriaDto.setFormType(d.getTypeForm());
            tempRecapitulationCriteriaDto.setNameAspect(getAspectNameInForm(d.getIndustryCriteria().getId(), d.getSeminarCriteria().getId(), d.getSupervisorCriteria().getId(), d.getSelfAssessmentCriteria().getId(), d.getNameForm()));
            tempRecapitulationCriteriaDto.setIdCriteria(d.getId());
            tempRecapitulationCriteriaDto.setValue(findValueByCriteriaIdAndParticipantId(d,participant.getId(),prodiId));

            tCriteriaDtos.add(tempRecapitulationCriteriaDto);
        });

        return tCriteriaDtos;
    }

    private Float findValueByCriteriaIdAndParticipantId(CriteriaComponentCourse criteria, Integer pId, Integer prodiId){

        Optional<CourseValues> courseValues = courseValuesRepository.findByCriteriaIdAndParticipantId(criteria.getId(), pId);
        CourseValues newValues = new CourseValues();
        float value = 0f;
        
        if(courseValues.isPresent()){
            if(criteria.getBobotCriteria() == 100){
                value = courseValues.get().getValue();
            }else{
                value = courseValues.get().getValue() * criteria.getBobotCriteria();
            }
            
        }else{
            // jika nilai blm dimasukan ke tabel
            switch(criteria.getNameForm()){
                case "Industri":
                //    mengambil nilai pada form 
                    Optional<Valuation> vIndustri = valuationRepository.findValueByParticipant(criteriaComponentCourseRepository.getNameAspectFromIndustry(criteria.getIndustryCriteria().getId()),Integer.parseInt(criteria.getTypeForm().substring(9)), pId);
                    if(vIndustri.isPresent()){
                        value = (float) vIndustri.get().getValue();
                        Optional<CriteriaComponentCourse> cccOptional = criteriaComponentCourseRepository.findById(criteria.getId());
                        newValues.setCriteriaComponentCourse(cccOptional.get());
                        newValues.setValuation(vIndustri.get());
                        newValues.setCreated_date(LocalDate.now());
                        Optional<Participant> pOptional = participantRepository.findById(pId);
                        newValues.setParticipant(pOptional.get());
                        newValues.setValue(value);

                        courseValuesRepository.save(newValues);
                    }else{
                        value = (float) 0;
                    }
                    break;
                case "Seminar":
                    Optional<SeminarValues> sv = seminarValuesRepository.findByTypeExaminer(criteria.getSeminarCriteria().getId(), pId, criteria.getTypeForm());

                    if(sv.isPresent()){
                        newValues.setCreated_date(LocalDate.now());
                        Optional<CriteriaComponentCourse> cccOptional = criteriaComponentCourseRepository.findById(criteria.getId());
                        newValues.setCriteriaComponentCourse(cccOptional.get());
                        newValues.setSeminarValues(sv.get());
                        Optional<Participant> pOptional = participantRepository.findById(pId);
                        newValues.setParticipant(pOptional.get());
                        newValues.setValue(sv.get().getValue());

                        courseValuesRepository.save(newValues);

                        value = sv.get().getValue();
                    }else{
                        value = (float) 0;
                    }
                    break;
                case "Pembimbing":
                    Optional<SupervisorGradeResult> sGrade = supervisorGradeResultRepository.findValueByPhase(criteria.getSupervisorCriteria().getId(), pId,criteria.getTypeForm().replaceAll("[^0-9]+", ""));
                    if(sGrade.isPresent()){
                        Optional<SupervisorGradeAspect> sAspect = supervisorGradeAspectRepository.findById(sGrade.get().getSupervisorGradeAspect().getId());
                        newValues.setCreated_date(LocalDate.now());
                        Optional<CriteriaComponentCourse> cccOptional = criteriaComponentCourseRepository.findById(criteria.getId());
                        newValues.setCriteriaComponentCourse(cccOptional.get());
                        newValues.setMentorValues(sGrade.get());
                        Optional<Participant> pOptional = participantRepository.findById(pId);
                        newValues.setParticipant(pOptional.get());

                        float sValue = (sGrade.get().getValue()/sAspect.get().getGradeWeight())*100;
                        newValues.setValue(sValue);

                        courseValuesRepository.save(newValues);

                        value = (float) sGrade.get().getValue();
                    }else{
                        value = 0f;
                    }
                    break;
                case "Self Assessment":
                    value = selfAssessmentGradeRepository.findValuesByCriteriaIdAndParticipantId(criteria.getSelfAssessmentCriteria().getId(), pId);
                    String prodiName = "";
                    if(prodiId == 0){
                        prodiName = "KP";
                    }else{
                        prodiName = "PKL";
                    }
                    
                    Optional<Timeline> timeline = timelineRepository.findByName("Pelaksanaan "+prodiName);

                    if(timeline.isPresent()){
                        LocalDate startDate = timeline.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        // Menyesuaikan tanggal awal dengan minggu ke-3
                        LocalDate week = startDate.with(TemporalAdjusters.dayOfWeekInMonth(Integer.parseInt(criteria.getTypeForm().replaceAll("[^0-9]+", "")), DayOfWeek.MONDAY));

                        Optional<SelfAssessmentGrade> selfAssessmentGrade = selfAssessmentGradeRepository.findByStartDate(criteria.getSelfAssessmentCriteria().getId(), pId, week.toString());
                        
                        if(selfAssessmentGrade.isPresent()){
                            newValues.setCreated_date(LocalDate.now());
                            Optional<CriteriaComponentCourse> cccOptional = criteriaComponentCourseRepository.findById(criteria.getId());
                            newValues.setCriteriaComponentCourse(cccOptional.get());
                            newValues.setSelfAssessmentValues(selfAssessmentGrade.get());
                            newValues.setValue((float) selfAssessmentGrade.get().getValueSelfAssessment());
                            Optional<Participant> pOptional = participantRepository.findById(pId);
                            newValues.setParticipant(pOptional.get());

                            courseValuesRepository.save(newValues);

                            value = (float) selfAssessmentGrade.get().getValueSelfAssessment();
                        }else{
                            value = (float) 0;
                        }
                    }
                    break;
            }
        }

        return value;
    }
    
    private String getAspectNameInForm(Integer idIndustri, Integer idSeminar, Integer idPembimbing, Integer idSelfAssessment, String formName){
        String aspectName = "";
        switch(formName){
            case "Industri":
                Optional<AssessmentAspect> aOptional = assessmentAspectRepository.findById(idIndustri);
                aspectName = aOptional.map(AssessmentAspect::getAspectName).orElse("");
                break;
            case "Seminar":
                Optional<SeminarCriteria> seminarCriteriaOptional = seminarCriteriaRepository.findById(idSeminar);
                aspectName = seminarCriteriaOptional.map(SeminarCriteria::getCriteriaName).orElse("");
                break;
            case "Pembimbing":
                Optional<SupervisorGradeAspect> supervisorGradeAspectOptional = supervisorGradeAspectRepository.findById(idPembimbing);
                aspectName = supervisorGradeAspectOptional.map(SupervisorGradeAspect::getDescription).orElse("");
                break;
            case "Self Assessment":
                Optional<SelfAssessmentAspect> selfAssessmentAspectOptional = selfAssessmentAspectRepository.findById(idSelfAssessment);
                aspectName = selfAssessmentAspectOptional.map(SelfAssessmentAspect::getName).orElse("");
                break;
            default:
                aspectName = "";
                break;
        }

        return aspectName;
    }

// versi 2 get recapitulation
    @Override
    public List<RecapitulationCourseDto> getRecapitulationV2(Integer year, Integer prodiId){
        List<RecapitulationCourseDto> rCourse = new ArrayList<>();
        List<CourseForm> courseForms = courseFormRepository.findByTahunAjaranStartAndProdiId(year, prodiId);
        List<Participant> participants = participantRepository.findByYearAndProdiId(year, prodiId);
        String prodiName = "";
        if(prodiId == 0){
            prodiName = "KP";
        }else{
            prodiName = "PKL";
        }
        
        Optional<Timeline> timeline = timelineRepository.findByName("Pelaksanaan "+prodiName);

        courseForms.forEach(cf -> {
            RecapitulationCourseDto rCourseTemp = new RecapitulationCourseDto();
            List<RecapitulationParticipantDto> rParticipants = new ArrayList<>();
            List<ComponentCourse> componentCourses = componentCourseRepository.findByCourse(cf);
            
            rCourseTemp.setIdCourse(cf.getId());
            rCourseTemp.setKode(cf.getKode());
            rCourseTemp.setNameCourse(cf.getName());
            
            participants.forEach(p -> {
                RecapitulationParticipantDto rParticipantTemp = new RecapitulationParticipantDto();
                List<RecapitulationComponentDto> rComponents = new ArrayList<>();

                rParticipantTemp.setIdParticipant(p.getId());
                rParticipantTemp.setName(p.getName());
                rParticipantTemp.setNim(p.getAccount().getUsername());

                componentCourses.forEach(cc -> {
                    RecapitulationComponentDto rComponentTemp = new RecapitulationComponentDto();
                    List<RecapitulationCriteriaDto> rCriterias = new ArrayList<>();
                    List<CriteriaComponentCourse> criterias = criteriaComponentCourseRepository.findByComponentCourse(cc);

                    rComponentTemp.setIdComponent(cc.getId());
                    rComponentTemp.setNameComponent(cc.getName());
                    rComponentTemp.setBobotComponent(cc.getBobotComponent());
                    
                    criterias.forEach(c -> {
                        RecapitulationCriteriaDto rCriteriaTemp = new RecapitulationCriteriaDto();
                        Optional<CourseValues> courseValue = courseValuesRepository.findByCriteriaComponentCourseAndParticipant(c, p);
                        String nameAspect = "";

                        if(c.getIndustryCriteria() != null){
                            nameAspect = c.getIndustryCriteria().getAspectName();
                        }else if(c.getSelfAssessmentCriteria() != null){
                            nameAspect = c.getSelfAssessmentCriteria().getName();
                        }else if(c.getSeminarCriteria() != null){
                            nameAspect = c.getSeminarCriteria().getCriteriaName();
                        }else if(c.getSupervisorCriteria() != null){
                            nameAspect = c.getSupervisorCriteria().getDescription();
                        }
                        rCriteriaTemp.setBobot(c.getBobotCriteria());
                        rCriteriaTemp.setFormType(c.getTypeForm());
                        rCriteriaTemp.setIdCriteria(c.getId());
                        rCriteriaTemp.setNameAspect(nameAspect);
                        rCriteriaTemp.setNameForm(c.getNameForm());

                        // menginputkan nilai peserta
                        if(courseValue.isPresent()){
                            if(cf.getIsFinalization() == 0){
                                // ambil nilai lagi
                                if(c.getIndustryCriteria() != null){
                                    Optional<Valuation> industryValue = valuationRepository.findByAspectNameAndEvaluation_ParticipantAndEvaluation_NumEvaluation(c.getIndustryCriteria().getAspectName(), p, c.getIndustryCriteria().getEvaluationForm().getNumEvaluation());
                                    courseValue.get().setValue((float) industryValue.get().getValue());
                                    rCriteriaTemp.setValue((float) industryValue.get().getValue());
                                }else if(c.getSelfAssessmentCriteria() != null){
                                    LocalDate startDate = timeline.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                    // Menyesuaikan tanggal awal dengan minggu ke-3
                                    LocalDate week = startDate.with(TemporalAdjusters.dayOfWeekInMonth(Integer.parseInt(c.getTypeForm().replaceAll("[^0-9]+", "")), DayOfWeek.MONDAY));

                                    Optional<SelfAssessmentGrade> selfAssessmentValue = selfAssessmentGradeRepository.findByStartDate(c.getSelfAssessmentCriteria().getId(), p.getId(), week.toString());
                                    courseValue.get().setValue((float) selfAssessmentValue.get().getValueSelfAssessment());
                                    rCriteriaTemp.setValue((float) selfAssessmentValue.get().getValueSelfAssessment());
                                }else if(c.getSeminarCriteria() != null){
                                    Optional<SeminarValues> seminarValue = seminarValuesRepository.findBySeminarCriteriaAndSeminarForm_ParticipantAndSeminarForm_ExaminerType(c.getSeminarCriteria(), p, Integer.parseInt(c.getTypeForm().replaceAll("[^0-9]+", "")));
                                    courseValue.get().setValue((float) seminarValue.get().getValue());
                                    rCriteriaTemp.setValue((float) seminarValue.get().getValue());
                                }else if(c.getSupervisorCriteria() != null){
                                    Optional<SupervisorGradeResult> supervisorValue = supervisorGradeResultRepository.findBySupervisorGradeAspectAndSupervisorGrade_ParticipantAndSupervisorGrade_Phase(c.getSupervisorCriteria(), p, Integer.parseInt(c.getTypeForm().substring(c.getTypeForm().lastIndexOf(" ") + 1)));
                                    courseValue.get().setValue((float) supervisorValue.get().getValue());
                                    rCriteriaTemp.setValue((float) supervisorValue.get().getValue());
                                }
                                courseValuesRepository.save(courseValue.get());
                            }else{
                                // dia ambil dari yang udah ada
                                rCriteriaTemp.setValue(courseValue.get().getValue());
                            }
                        }else{
                            // buat row baru
                            // ambil nilai lagi
                            CourseValues newCourseValue = new CourseValues();
                            newCourseValue.setCreated_date(LocalDate.now());
                            newCourseValue.setCriteriaComponentCourse(c);
                            newCourseValue.setParticipant(p);
                            
                            if(c.getIndustryCriteria() != null){
                                Optional<Valuation> industryValue = valuationRepository.findByAspectNameAndEvaluation_ParticipantAndEvaluation_NumEvaluation(c.getIndustryCriteria().getAspectName(), p, c.getIndustryCriteria().getEvaluationForm().getNumEvaluation());
                                newCourseValue.setValue((float) industryValue.get().getValue());
                                newCourseValue.setValuation(industryValue.get());
                                rCriteriaTemp.setValue((float) industryValue.get().getValue());
                            }else if(c.getSelfAssessmentCriteria() != null){
                                LocalDate startDate = timeline.get().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                // Menyesuaikan tanggal awal dengan minggu ke-3
                                LocalDate week = startDate.with(TemporalAdjusters.dayOfWeekInMonth(Integer.parseInt(c.getTypeForm().replaceAll("[^0-9]+", "")), DayOfWeek.MONDAY));

                                Optional<SelfAssessmentGrade> selfAssessmentValue = selfAssessmentGradeRepository.findByStartDate(c.getSelfAssessmentCriteria().getId(), p.getId(), week.toString());
                                newCourseValue.setValue((float) selfAssessmentValue.get().getValueSelfAssessment());
                                newCourseValue.setSelfAssessmentValues(selfAssessmentValue.get());
                                rCriteriaTemp.setValue((float) selfAssessmentValue.get().getValueSelfAssessment());
                            }else if(c.getSeminarCriteria() != null){
                                Optional<SeminarValues> seminarValue = seminarValuesRepository.findBySeminarCriteriaAndSeminarForm_ParticipantAndSeminarForm_ExaminerType(c.getSeminarCriteria(), p, Integer.parseInt(c.getTypeForm().replaceAll("[^0-9]+", "")));
                                newCourseValue.setValue((float) seminarValue.get().getValue());
                                newCourseValue.setSeminarValues(seminarValue.get());
                                rCriteriaTemp.setValue((float) seminarValue.get().getValue());
                            }else if(c.getSupervisorCriteria() != null){
                                Optional<SupervisorGradeResult> supervisorValue = supervisorGradeResultRepository.findBySupervisorGradeAspectAndSupervisorGrade_ParticipantAndSupervisorGrade_Phase(c.getSupervisorCriteria(), p, Integer.parseInt(c.getTypeForm().substring(c.getTypeForm().lastIndexOf(" ") + 1)));
                                newCourseValue.setValue((float) supervisorValue.get().getValue());
                                newCourseValue.setMentorValues(supervisorValue.get());
                                rCriteriaTemp.setValue((float) supervisorValue.get().getValue());
                            }

                            courseValuesRepository.save(newCourseValue);
                        }
                        
                        rCriterias.add(rCriteriaTemp);
                    });
                    Optional<TotalComponents> totalComponent = totalComponentsRepository.findByComponentCourseAndParticipant(cc, p);
                    if(totalComponent.isPresent()){
                        if(cf.getIsFinalization() != 1){
                            totalComponent.get().setValue(getTotalComponent(cc.getIsAverage(), rCriterias));
                            totalComponentsRepository.save(totalComponent.get());
                        }
                        rComponentTemp.setTotalValueComponent(totalComponent.get().getValue());
                    }else{
                        TotalComponents newTotalComponent = new TotalComponents();
                        newTotalComponent.setComponentCourse(cc);
                        newTotalComponent.setParticipant(p);
                        newTotalComponent.setValue(getTotalComponent(cc.getIsAverage(), rCriterias));
                        totalComponentsRepository.save(newTotalComponent);
                        rComponentTemp.setTotalValueComponent(newTotalComponent.getValue());
                    }
                    
                    rComponentTemp.setCriteria_data(rCriterias);
                    rComponents.add(rComponentTemp);
                });
                
                Optional<TotalCourses> totalCourse = totalCoursesRepository.findByCourseFormAndParticipant(cf, p);
                if(totalCourse.isPresent()){
                    if(cf.getIsFinalization() != 1){
                        totalCourse.get().setValue(getTotalCourse(rComponents));
                        totalCoursesRepository.save(totalCourse.get());
                    }
                    rParticipantTemp.setTotal_course(totalCourse.get().getValue());
                }else{
                    TotalCourses newTotalCourse = new TotalCourses();
                    newTotalCourse.setCourseForm(cf);
                    newTotalCourse.setParticipant(p);
                    newTotalCourse.setValue(getTotalCourse(rComponents));
                    totalCoursesRepository.save(newTotalCourse);
                    rParticipantTemp.setTotal_course(newTotalCourse.getValue());
                }
                rParticipants.add(rParticipantTemp);
            });
            
            rCourseTemp.setParticipant_data(rParticipants);
            rCourse.add(rCourseTemp);
        });
        return rCourse;
    }

    @Override
    public void finalizationAllCourseForm(Integer prodiId) {
        List<CourseForm> courseAll = courseFormRepository.findAllCourse(Integer.parseInt(Year.now().toString()));

        courseAll.forEach(c -> {
            if(c.getProdiId() == prodiId){
                c.setIsFinalization(1);
                courseFormRepository.save(c);
            }
        });
    }
    
    @Override
    public ByteArrayInputStream loadCourse(Integer year, Integer prodiId) {
        List<CourseForm> courseForms = new ArrayList<>();

        if(Integer.parseInt(Year.now().toString()) == year){
            courseForms = courseFormRepository.findAllCourseByYearAndProdiId(year, prodiId);
        }else{
            courseForms = courseFormRepository.findAllOldCourseByYearAndProdiId(year, prodiId);
        }
        List<List<ComponentAndCriteriasDto>> listCC = new ArrayList<>();
        for(CourseForm course : courseForms){
            List<ComponentAndCriteriasDto> temp = getCriteriaComponentByCourseFormId(course.getId());
            listCC.add(temp);
        }
        List<RecapitulationCourseDto> recap = getAllRecapitulationByYearAndProdiId(year, prodiId);
        ByteArrayInputStream in = ExcelHelper.recapCoursetoExcel(recap, listCC);
        return in;
    }

    @Override
    public IsFinalizationDto isFinalization(Integer prodiId){
        IsFinalizationDto finalizationDto = new IsFinalizationDto();

        finalizationDto.setIsFinalization(courseFormRepository.isAllFinalizationByYear(Integer.parseInt(Year.now().toString()), prodiId));
        return finalizationDto;
    }

    @Override
    public void cancelFinalization(Integer prodiId){
        List<CourseForm> courseAll = courseFormRepository.findAllCourse(Integer.parseInt(Year.now().toString()));

        courseAll.forEach(c -> {
            if(c.getProdiId() == prodiId){
                c.setIsFinalization(0);
                courseFormRepository.save(c);
            }
        });
    }

    @Override
    public List<EventStoreDto> getEventStore(){
        List<EventStore> eventStores = eventStoreRepository.findAllTenEventDesc();
        List<EventStoreDto> dto = new ArrayList<>();

        eventStores.forEach(e -> {
            EventStoreDto temp = new EventStoreDto();
            temp.setEntityId(e.getEntityId());
            temp.setEventTime(e.getEventTime());
            temp.setEventType(e.getEventType());
            temp.setId(e.getId());

            dto.add(temp);
        });
        return dto;
    }

    @Override
    public List<EvaluationIndustryDto> getKafkaEvaluationIndustry(){
        List<EvaluationIndustryDto> dtos = new ArrayList<>();
        List<Evaluation> evaluations = evaluationRepository.findAllByUpdateDate();

        evaluations.forEach(e -> {
            EvaluationIndustryDto tempDto = new EvaluationIndustryDto();

            List<Valuation> valuations = valuationRepository.findByEvaluationId(e.getId());

            tempDto.setComment(e.getComment());
            tempDto.setId(e.getId());
            tempDto.setPosition(e.getPosition());
            tempDto.setProdiId(e.getProdiId());
            tempDto.setNumEvaluation(e.getNumEvaluation());
            tempDto.setYear(e.getYear());
            tempDto.setCompany(e.getCompany());
            tempDto.setParticipant(e.getParticipant());
            tempDto.setValuations(valuations);

            dtos.add(tempDto);
            
        });

        return dtos;
    }
}
