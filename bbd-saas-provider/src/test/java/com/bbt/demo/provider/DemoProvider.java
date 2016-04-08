/*
 * Copyright 1999-2011 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbt.demo.provider;

import com.google.common.collect.Lists;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DemoProvider {

    public static void main(String[] args) throws IOException {
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//        ctx.register(ProviderConfig.class);
//        ctx.refresh();
//        System.in.read();

        try {
            ServerAddress serverAddress = new ServerAddress("182.92.227.140", 27017);
            MongoCredential mongoCredential = MongoCredential.createCredential("expressadmin","express","expressadminpass".toCharArray());
            List<MongoCredential> mongoCredentialList = Lists.newArrayList();
            mongoCredentialList.add(mongoCredential);
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient(serverAddress,mongoCredentialList);
            //根据mongodb数据库的名称获取mongodb对象 ,
            MongoDatabase db = mongoClient.getDatabase("express");
            MongoIterable<String> collectionNames = db.listCollectionNames();
            // 打印出test中的集合
            for (String name : collectionNames) {
                System.out.println("collectionName===" + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}