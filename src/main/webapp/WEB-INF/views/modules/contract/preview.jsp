<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文档预览</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {

                    $("#preview-box").show();


                    var fp = new FlexPaperViewer(
                        '${ctxStatic}/flexpaper/FlexPaperViewer',
                        'viewerPlaceHolder', { config : {

                            SwfFile : '${file}',
                            Scale : 1,
                            ZoomTransition : 'easeOut',
                            ZoomTime : 0.5,
                            ZoomInterval : 0.2,
                            FitPageOnLoad : true,
                            FitWidthOnLoad :true,
                            PrintEnabled : true,
                            FullScreenAsMaxWindow : false,
                            ProgressiveLoading : false,
                            MinZoomSize : 0.2,
                            MaxZoomSize : 5,
                            SearchMatchAll : true,
                            InitViewMode : 'Portrait',
                            ViewModeToolsVisible : true,
                            ZoomToolsVisible : true,
                            NavToolsVisible : true,
                            CursorToolsVisible : true,
                            SearchToolsVisible : true,
                            localeChain: 'en_US'
                        }});

		});

	</script>
</head>
<body>
<div  id="preview-box" style="width:100%;height:100%;">
	<%--<a id="viewerPlaceHolder" style="width:800px;height:504px;display:block"></a>--%>
	<a id="viewerPlaceHolder" style="width:800px;height:750px;display:block"></a>
</div>
</body>
</html>