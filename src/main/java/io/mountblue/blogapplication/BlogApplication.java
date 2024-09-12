package io.mountblue.blogapplication;

import io.mountblue.blogapplication.dto.PostDTO;
import io.mountblue.blogapplication.dto.PostSummaryDTO;
import io.mountblue.blogapplication.entity.Post;
import io.mountblue.blogapplication.entity.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Bean
    ModelMapper getmodelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

//        modelMapper.createTypeMap(Post.class, PostDTO.class).addMappings(mapper -> {
//            mapper.map(post -> post.getTagNames(), PostDTO::setTagsList);
//            mapper.map(post -> post.getAuthor().getFullName(), PostDTO::setAuthor);
//        });

//        modelMapper.createTypeMap(Post.class, PostSummaryDTO.class).addMappings(mapper -> {
//            mapper.map(post -> post.getAuthor()
//                            .getFullName(),
//                    PostSummaryDTO::setAuthor);
//        });

        return modelMapper;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_AUTHOR";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

}
