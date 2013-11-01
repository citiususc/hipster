<?cs # placeholder for custom clearsilver code. ?>
<?cs def:custom_masthead() ?>
<div id="header">
    <div id="headerLeft">
      <a href="http://citius.usc.es" tabindex="-1"><img src="<?cs var:toassets ?>images/hipster-logo.png" alt="" border="0"></a>
    </div>
    <div id="headerRight">
      <?cs call:default_search_box() ?>
      <?cs if:reference && reference.apilevels ?>
        <?cs call:default_api_filter() ?>
      <?cs /if ?>
    </div>
</div><!-- header -->
<?cs /def ?>