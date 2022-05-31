package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.repository.UserRepository;
import yy.project.YOYO.vo.UserVO;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public int memberCheck(String userID){
        User user = userRepository.findByUserID(userID);

        if(userID==""){
            return -1;
        }else if(user==null){
            return 0;
        }else{
            return 1;
        }
    }

    public User findByUserID(String userID){
        return userRepository.findByUserID(userID);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public String filePathForUserProfileImage(List<MultipartFile> images) throws Exception{

        String userPhoto = null;

        if(!CollectionUtils.isEmpty(images)){ //이미지 파일이 존재할 경우
            //프로젝트 내의 static 폴더까지의 절대 경로
            String absolutePath = new File("").getAbsolutePath()+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static";

            for(MultipartFile image : images){
                String originalFileExtension = new String();
                String contentType = image.getContentType();

                if(ObjectUtils.isEmpty(contentType)){ //확장자가 없는 파일 -> 처리 x
                    break;
                }
                else{
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        break;
                }

                String newFileName = System.nanoTime()+originalFileExtension; //이미지 이름이 겹치지 않게 나노시간을 이름으로 사진 저장
                File file = new File(absolutePath+File.separator+"userProfileImage"+File.separator+newFileName);
                image.transferTo(file);
                file.setWritable(true);
                file.setReadable(true);

                userPhoto = File.separator + "userProfileImage" + File.separator + newFileName;

            }
        }
        return userPhoto;
    }

    public void updateUser(String userID, UserVO form) throws Exception {

        String userImage = filePathForUserProfileImage(form.getUserImage());

        User user = findByUserID("rabbith3");
        user.setUserID(form.getUserID());
        user.setPassword(form.getPassword());
        user.setUserName(form.getUserName());
        user.setEmail(form.getEmail());
        user.setAddress(form.getAddress());

        if(userImage != null){
            user.setUserImage(userImage);
        }

        userRepository.save(user);

    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
