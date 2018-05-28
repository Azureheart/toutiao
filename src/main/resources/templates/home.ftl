<#include "header.ftl">

    <div id="main">

        <div class="container" id="daily">
            <div class="jscroll-inner">
                <div class="daily">
                    <#assign cur_date=''>
                    <#list vos as vo>
                    <#if cur_date!=(vo.news.createdDate)?string("yyyy-MM-dd")>
                        <#if vo?is_first!=true>
                            </div><#--第一个vo开始啦-->
                        </#if>
                    <#assign cur_date=(vo.news.createdDate)?string("yyyy-MM-dd")>
                    <h3 class="date">
                        <i class="fa icon-calendar"></i>
                        <span>头条资讯 &nbsp; ${(vo.news.createdDate)?string("yyyy-MM-dd")}</span>
                    </h3>

                     <div class="posts">
                     </#if>
            <div class="post">
                <div class="votebar">
                    <#if vo.like gt 0>
                    <button class="click-like up pressed" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                    <#else>
                    <button class="click-like up" data-id="${vo.news.id}" title="赞同"><i class="vote-arrow"></i><span class="count">${vo.news.likeCount}</span></button>
                    </#if>
                    <#if vo.like lt 0>
                    <button class="click-dislike down pressed" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                    <#else >
                    <button class="click-dislike down" data-id="${vo.news.id}" title="反对"><i class="vote-arrow"></i></button>
                    </#if>
                </div>
                <div class="content" data-url="http://nowcoder.com/posts/5l3hjr">
                    <div >
                        <img class="content-img" src="${vo.news.image}?imageMogr2/auto-orient/thumbnail/100x80!/blur/1x0/quality/75|imageslim" alt="">
                    </div>
                    <div class="content-main">
                        <h3 class="title">
                            <a target="_blank" rel="external nofollow" href="/news/${vo.news.id}">${vo.news.title}</a>
                        </h3>
                        <div class="meta">
                            ${vo.news.link}
                            <span>
                              <i class="fa icon-comment"></i> ${vo.news.commentCount}
                            </span>
                        </div>
                    </div>
                </div>
                <div class="user-info">
                    <div class="user-avatar">
                        <a href="/user/${vo.user.id}/"><img width="32" class="img-circle" src="${vo.user.headUrl}"></a>
                    </div>

                </div>

                <div class="subject-name">来自 <a href="/user/${vo.user.id}/">${vo.user.name}</a></div>
            </div>

                <#if vo?is_last==true>
                </div><#--最后一个循环项-->
                </#if>
                </#list>
            </div>
        </div>
    </div>


<#if pop??>
<script>
window.loginpop = ${pop!};
</script>
</#if>
<script type="text/javascript" src="/scripts/main/site/home.js"></script>

<#include "footer.ftl">