package org.woehlke.twitterwall.frontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.twitterwall.configuration.properties.TwitterProperties;
import org.woehlke.twitterwall.configuration.properties.FrontendProperties;
import org.woehlke.twitterwall.frontend.content.Symbols;
import org.woehlke.twitterwall.frontend.content.ContentFactory;
import org.woehlke.twitterwall.oodm.model.transients.HashTagCounted;
import org.woehlke.twitterwall.oodm.model.Tweet;
import org.woehlke.twitterwall.oodm.model.User;
import org.woehlke.twitterwall.oodm.model.HashTag;
import org.woehlke.twitterwall.oodm.service.TweetService;
import org.woehlke.twitterwall.oodm.service.UserService;
import org.woehlke.twitterwall.oodm.service.HashTagService;

import javax.persistence.EntityNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.woehlke.twitterwall.frontend.content.ContentFactory.FIRST_PAGE_NUMBER;
import static org.woehlke.twitterwall.oodm.model.HashTag.HASHTAG_TEXT_PATTERN;

/**
 * Created by tw on 12.07.17.
 */
@Controller
@RequestMapping(path="/hashtag")
public class HashTagController {

    @RequestMapping(path="/all")
    public String getAll(
            @RequestParam(name= "page" ,defaultValue=""+ FIRST_PAGE_NUMBER) int page,
            Model model
    ) {
        String subtitle = "all";
        String title = "HashTag";
        String symbol = Symbols.HASHTAG.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        Pageable pageRequest = new PageRequest(
                page,
                frontendProperties.getPageSize(),
                Sort.Direction.ASC,
                "text"
        );
        Page<HashTag> myPageContent = hashTagService.getAll(pageRequest);
        model.addAttribute("myPageContent",myPageContent);
        return "hashtag/all";
    }

    @RequestMapping(path="/{id}")
    public String findHashTagById(
            @PathVariable("id") HashTag hashTag,
            @RequestParam(name= "pageTweet" ,defaultValue=""+ FIRST_PAGE_NUMBER) int pageTweet,
            @RequestParam(name= "pageUser" ,defaultValue=""+ FIRST_PAGE_NUMBER) int pageUser,
            Model model
    ) {
        if(hashTag == null){
            throw new EntityNotFoundException();
        } else {
            String msg = "/hashtag/" + hashTag.getId()+ " ";
            String msg2 = msg + " parameter IS valid - START ";
            log.debug(msg2);
            Pageable pageRequestTweet = new PageRequest(pageTweet, frontendProperties.getPageSize());
            Pageable pageRequestUser = new PageRequest(pageUser, frontendProperties.getPageSize());
            String subtitle = "Tweets und User für HashTag";
            String title = hashTag.getText();
            String symbol = Symbols.HASHTAG.toString();
            model = contentFactory.setupPage(model, title, subtitle, symbol);
            model.addAttribute("hashTag",hashTag);
            log.debug(msg+" try to: tweetService.findTweetsForHashTag: ");
            Page<Tweet> tweets = tweetService.findTweetsForHashTag(hashTag,pageRequestTweet);
            model.addAttribute("latestTweets", tweets);
            log.debug(msg+" try to: userService.getUsersForHashTag: ");
            Page<User> users = userService.getUsersForHashTag(hashTag,pageRequestUser);
            model.addAttribute("users", users);
            log.debug(msg + " READY - DONE");
            return "hashtag/id";
        }
    }

    @RequestMapping(path="/text/{text}")
    public String hashTagFromTweetsAndUsers(
        @PathVariable("text") String text,
        @RequestParam(name= "pageTweet" ,defaultValue=""+ FIRST_PAGE_NUMBER) int pageTweet,
        @RequestParam(name= "pageUser" ,defaultValue=""+ FIRST_PAGE_NUMBER) int pageUser,
         Model model
    ) {
        String msg = "/hashtag/" + text + " ";
        Pattern p = Pattern.compile(HASHTAG_TEXT_PATTERN);
        Matcher m = p.matcher(text);
        if (m.matches()) {
            String msg2 = msg + " parameter IS valid - START ";
            log.debug(msg2);
            Pageable pageRequestTweet = new PageRequest(pageTweet, frontendProperties.getPageSize());
            Pageable pageRequestUser = new PageRequest(pageUser, frontendProperties.getPageSize());
            String subtitle = "Tweets und User für HashTag";
            String title = text;
            String symbol = Symbols.HASHTAG.toString();
            model = contentFactory.setupPage(model, title, subtitle, symbol);
            log.debug(msg + " try to: hashTagService.findByText ");
            HashTag hashTag = hashTagService.findByText(text);
            model.addAttribute("hashTag",hashTag);
            log.debug(msg + " found:  " + text);
            log.debug(msg+" try to: tweetService.findTweetsForHashTag: ");
            Page<Tweet> tweets = tweetService.findTweetsForHashTag(hashTag,pageRequestTweet);
            model.addAttribute("latestTweets", tweets);
            log.debug(msg+" try to: userService.getUsersForHashTag: ");
            Page<User> users = userService.getUsersForHashTag(hashTag,pageRequestUser);
            model.addAttribute("users", users);
            log.debug(msg + " READY - DONE");
            return "hashtag/id";
        } else {
            String msg2 = msg + " parameter ist NOT valid";
            log.warn(msg2);
            return "hashtag/id";
        }
    }

    @RequestMapping(path="/overview")
    public String hashTagsOverview(
            @RequestParam(name= "pageTweet", defaultValue=""+ FIRST_PAGE_NUMBER) int pageTweet,
            @RequestParam(name= "pageUser", defaultValue=""+ FIRST_PAGE_NUMBER) int pageUser,
            Model model) {
        String msg = "/hashtag/overview ";
        String title = "HashTags";
        String subtitle = twitterProperties.getSearchQuery();
        String symbol = Symbols.HASHTAG.toString();
        model = contentFactory.setupPage(model,title,subtitle,symbol);
        int size= frontendProperties.getPageSize() * 2;
        Pageable pageRequestTweets = new PageRequest(pageTweet,size);
        Pageable pageRequestUsers = new PageRequest(pageUser,size);
        Page<HashTagCounted> hashTagsTweets = hashTagService.getHashTagsTweets(pageRequestTweets);
        Page<HashTagCounted> hashTagsUsers = hashTagService.getHashTagsUsers(pageRequestUsers);
        model.addAttribute("hashTagsTweets", hashTagsTweets);
        model.addAttribute("hashTagsUsers", hashTagsUsers);
        return "hashtag/overview";
    }

    private static final Logger log = LoggerFactory.getLogger(HashTagController.class);

    private final FrontendProperties frontendProperties;

    private final TwitterProperties twitterProperties;

    private final HashTagService hashTagService;

    private final TweetService tweetService;

    private final UserService userService;

    private final ContentFactory contentFactory;

    @Autowired
    public HashTagController(
            FrontendProperties frontendProperties,
            TwitterProperties twitterProperties,
            HashTagService hashTagService,
            TweetService tweetService,
            UserService userService,
            ContentFactory contentFactory
    ) {
        this.frontendProperties = frontendProperties;
        this.twitterProperties = twitterProperties;
        this.hashTagService = hashTagService;
        this.tweetService = tweetService;
        this.userService = userService;
        this.contentFactory = contentFactory;
    }

}
