package com.linkdoan.backend.service.impl;

import com.linkdoan.backend.dto.SubjectDTO;
import com.linkdoan.backend.model.Subject;
import com.linkdoan.backend.repository.EducationProgramSubjectRepository;
import com.linkdoan.backend.repository.SubjectRepository;
import com.linkdoan.backend.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("subjectService")
public class SubjectServiceImpl implements SubjectService {

    @Qualifier("subjectRepository")
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EducationProgramSubjectRepository educationProgramSubjectRepository;

    @Override
    public Page findBy(Pageable pageable, SubjectDTO subjectDTO) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getAll() {
        List<Object[]> subjectObjectList = subjectRepository.getAll();
        List<Map<String, Object>> subjectListMap =  new ArrayList<>();
        if(!subjectObjectList.isEmpty()){
            for(Object[] object : subjectObjectList){
                Map<String, Object> subjectMap = new HashMap<>();
                subjectMap.put("departmentId", object[0]);
                subjectMap.put("discussNumber", object[1]);
                subjectMap.put("eachSubject", object[2]);
                subjectMap.put("exerciseNumber", object[3]);
                subjectMap.put("practiceNumber", object[4]);
                subjectMap.put("selfLearningNumber", object[5]);
                subjectMap.put("subjectForLevel", object[6]);
                subjectMap.put("subjectId", object[7]);
                subjectMap.put("subjectName", object[8]);
                subjectMap.put("theoryNumber", object[9]);
                subjectMap.put("departmentName", object[10]);
                List<Object[]> preLearnSubjectList = subjectRepository.getPreviousLearnSubject((String) object[0]);
                if(!preLearnSubjectList.isEmpty() && preLearnSubjectList != null){
                    List<Map<String, Object>> preLearnList = new ArrayList<>();
                    for(Object[] preObject : preLearnSubjectList){
                        Map<String, Object> preObjectMap = new HashMap<>();
                        preObjectMap.put("subjectId", preObject[0]);
                        preObjectMap.put("subjectName", preObject[1]);
                        preLearnList.add(preObjectMap);
                    }
                    subjectMap.put("preLearnSubjectList",preLearnSubjectList );
                }
                else{
                    subjectMap.put("preLearnSubjectList",new ArrayList<>() );
                }
                subjectListMap.add(subjectMap);
            }
        }
        return subjectListMap;
    }

    @Override
    public int create(List<SubjectDTO> subjectDTOList) {
        int count  = 0 ;
        for(SubjectDTO subjectDTO : subjectDTOList){
            if(!subjectRepository.findById(subjectDTO.getSubjectId()).isPresent()){
                subjectRepository.save(subjectDTO.toModel());
                count++;
            }
        }
        return count;
    }

    @Override
    public Subject update(String id, SubjectDTO subjectDTO) {
        if(!subjectRepository.findById(subjectDTO.getSubjectId()).isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tồn tại!!!");
        return subjectRepository.save(subjectDTO.toModel());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(List<String> subjectDTOList) {
        int count  = 0 ;
        for(String id : subjectDTOList){
            if(subjectRepository.findById(id).isPresent()){
                subjectRepository.deleteById(id);
                count++;
            }
        }
        return count;
    }

}
