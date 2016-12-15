package controller;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class Controller {

    private Set<String> emptySet = Sets.newHashSet();
    private Map<String, Set<String>> get;
    private Set<String> post;

    public Controller() {
        this.get = new HashMap<>();
        this.post = new HashSet<>();

        //GET
        Set<String> parameter = new HashSet<>();
        parameter.add("userId");
        parameter.add("password");
        parameter.add("name");
        parameter.add("email");
        this.get.put("/user/create", parameter);

        //POST
        post.add("/user/create");
        post.add("/user/login");
    }

    public boolean matchGet(String url){
        return get.containsKey(url);
    }

    public boolean matchPost(String url){
        return post.contains(url);
    }

    public Set<String> getParameters(String url){
        return get.get(url);
    }
}
