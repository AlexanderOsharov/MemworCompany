package com.shurik.memwor_24.memwor.api.module_reddit;

import com.shurik.memwor_24.memwor.content.PostData;

import java.util.List;

public class RedditResponse {
    public RedditData data;

    public class RedditData {
        public List<RedditPost> children;

        public class RedditPost {
            public PostData data;
        }
    }
}

