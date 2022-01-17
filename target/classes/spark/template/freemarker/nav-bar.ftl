 <div class="navigation">
  <#if currentUser??>
    <a href="/">my home</a> |
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">sign out [${currentUser.name}]</a>
    </form>
  <#elseif currentPlayer??>
          <form id="signout" action="/signout" method="post">
                <a href="#" onclick="event.preventDefault(); signout.submit();">sign out</a>
            </form>
  <#else>
    <a href="/signin">Sign in</a>
  </#if>
 </div>
