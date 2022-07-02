<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="#">
    <title>FLOW - 파일 확장자 차단</title>

    <link rel="stylesheet" href="/flow/resources/css/common.css">
    <script type="text/javascript" src="/flow/resources/jQuery/jquery-3.6.0.min.js"></script>

</head>

<body>
<div class="loading-page" style="display: none;">
    <img src="/flow/resources/img/loading-icon.png" alt="loading...">
</div>
<section>
    <h1> ⛔ 파일 확장자 차단 </h1>
    <hr>
    <table class="extension-tbl">
        <tbody>
        <tr>
            <th>Function 선택</th>
            <td>
                <select class="box" id="funcSelBox">
<%--                    <option value="1">1</option>--%>
<%--                    <option value="2">2</option>--%>
<%--                    <option value="3">3</option>--%>
                </select>
            </td>
        </tr>
        <tr class="fixed-ex-row">
            <th>고정 확장자</th>
            <td>
<%--                <span><input type="checkbox" name="bat" id="fixedEx01"><label for="fixedEx01">bat</label></span>--%>
<%--                <span><input type="checkbox" name="cmd" id="fixedEx02"><label for="fixedEx02">cmd</label></span>--%>
<%--                <span><input type="checkbox" name="com" id="fixedEx03"><label for="fixedEx03">com</label></span>--%>
<%--                <span><input type="checkbox" name="cpl" id="fixedEx04"><label for="fixedEx04">cpl</label></span>--%>
<%--                <span><input type="checkbox" name="exe" id="fixedEx05"><label for="fixedEx05">exe</label></span>--%>
<%--                <span><input type="checkbox" name="scr" id="fixedEx06"><label for="fixedEx06">scr</label></span>--%>
<%--                <span><input type="checkbox" name="js" id="fixedEx07"><label for="fixedEx06">js</label></span>--%>
            </td>
        </tr>
        <tr class="custom-ex-row">
            <th>커스텀 확장자</th>
            <td>
                <div style="position: relative;">
                    <input class="box" type="text" id="customExTxt" placeholder="확장자 입력">
                    <input class="box" type="button" value="+추가" onclick="addCustomExtension()">
                    <input class="box delete-btn" type="button" value="전체 확장자 삭제" onclick="deleteAllExtension()">
                </div>
                <div class="box">
                    <div id="totalCnt">0 / 200</div>
                    <div id="customExList">

                    </div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</section>
</body>

<script>
    let g_functionList;
    let g_defaultExtensionList;
    let g_funcKey;

    $(document).ready(function() {
        initData();
        initEventListener();
    });

    function initData(){
        g_functionList = ${functionList};
        g_defaultExtensionList = ${defaultExtensionList};

        displayFunctionSelBox();
        displayDefaultCheckBox();

        g_funcKey = $('#funcSelBox option:selected').val();

        getExtensionList();
    }
    function initEventListener(){
        $('#funcSelBox').change((event)=>{
            g_funcKey = parseInt(event.target.value);

            // Init default extension
            $('.fixed-ex-row td').empty();
            displayDefaultCheckBox();
            setCheckBoxEventListener();

            // Init custom extension
            $('#customExList').empty();
            displayCount();

            getExtensionList();
        });

        $('#customExTxt').keyup((event)=>{
           if(event.keyCode == 13){
               addCustomExtension();
           }
        });

        // default checkbox는 최초 세팅 이후에 이벤트 리스너 추가
        setCheckBoxEventListener();
    }
    function setCheckBoxEventListener(){
        $('.fixed-ex-row input[type=checkbox]').change((event)=>{
            let code = $(event.target).attr('name');
            if($(event.target).prop('checked')){    // save extension
                addDefaultExtension(code);
            }else{  // delete Extension
                deleteDefaultExtension(code);
            }
        });
    }
    function checkCharacter(element){
        var regExp = /[\{\}\[\]\/?.,;:|\)~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;   //  * 제외
        if (regExp.test(element.value)) {
            element.value = element.value.replace(regExp , '');
            alert("*를 제외한 특수 문자를 사용할 수 없습니다.");
        }
    }

    function displayFunctionSelBox(){
        let innerHtml = "";
        for(let func of g_functionList){
            innerHtml += "<option value=\"" + func.funcKey + "\">" + func.name + " - " + func.description + "</option>";
        }

        $('#funcSelBox').append(innerHtml);
    }
    function displayDefaultCheckBox(){
        let innerHtml = "";
        for(let extension of g_defaultExtensionList){
            innerHtml += "<span><input type=\"checkbox\" name=\"" + extension.code + "\" id=\"fixedEx"+ extension.exKey +"\"><label for=\"fixedEx"+ extension.exKey +"\">" + extension.code + "</label></span>";
        }

        $('.fixed-ex-row td').append(innerHtml);
    }

    function getExtensionList(){
        $('.loading-page').show();

        let url = "/flow/functions/"+ g_funcKey +"/extensions";

        $.ajax({
            url:url,
            type : 'GET',
            dataType:'json',
            contentType: 'application/json',
            success : function(data){
                displayExtensionList(data.extensionList);
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }
    function displayExtensionList(extensionList){
        for(let extension of extensionList){
            if(extension.defaultYn){// default
                $("input[name=" + extension.code + "]").prop('checked',true);
            }else{  //custom
                displayCustomExtension(extension.code)
            }
        }

        displayCount();
    }
    function displayCustomExtension(code){
        let innerHtml = "<div class=\"ex-item\"><span>" + code + "</span><button onclick=\"deleteCustomExtension()\">×</button></div>";
        $('#customExList').append(innerHtml);
    }

    function addDefaultExtension(code){
        $('.loading-page').show();

        let url = "/flow/extensions/";
        let param = {};
        param.funcKey = g_funcKey;
        param.code = code;

        $.ajax({
            url:url,
            type : 'POST',
            dataType:'json',
            data : JSON.stringify(param),
            contentType: 'application/json',
            success : function(data){
                if(!data.result){
                    alert(data.msg);
                }
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }
    function addCustomExtension(){
        let tempExtension = $('#customExTxt').val();

        // 제한사항 체크
        if($('.ex-item').length >= 200){
            return alert("커스텀 확장자는 최대 200개 까지 추가할 수 있습니다.");
        }
        if(tempExtension.length > 20 || tempExtension.length == 0){
            return alert("커스텀 확장자의 길이는 최소 1자, 최대 20자 입니다.");
        }
        if(g_defaultExtensionList.filter(ele=> ele.code === tempExtension).length > 0){
            return alert("해당 확장자는 고정 확장자 목록에 포함되어있습니다. 상단의 고정 확장자 목록에서 확인해주세요.");
        }

        $('.loading-page').show();

        let url = "/flow/extensions/";
        let param = {};
        param.funcKey = g_funcKey;
        param.code = tempExtension;

        $.ajax({
            url:url,
            type : 'POST',
            dataType:'json',
            data : JSON.stringify(param),
            contentType: 'application/json',
            success : function(data){
                if(data.result){
                    // customExList에 추가
                    displayCustomExtension(tempExtension);

                    // 입력 폼 초기화
                    $('#customExTxt').val("");
                    displayCount();
                }else{
                    alert(data.msg);
                }
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }

    function deleteAllExtension(){
        $('.loading-page').show();

        let url = "/flow/extensions/" + g_funcKey;

        $.ajax({
            url:url,
            type : 'DELETE',
            dataType:'json',
            contentType: 'application/json',
            success : function(data){
                if(data.result) {
                    // Init default extension
                    $('.fixed-ex-row td').empty();
                    displayDefaultCheckBox();
                    setCheckBoxEventListener();

                    // Init custom extension
                    $('#customExList').empty();
                    displayCount();
                }else{
                    alert(data.msg);
                }
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }
    function deleteDefaultExtension(code){
        $('.loading-page').show();

        let url = "/flow/extensions/" + g_funcKey + "," + code;

        $.ajax({
            url:url,
            type : 'DELETE',
            dataType:'json',
            contentType: 'application/json',
            success : function(data){
                if(!data.result) {
                    alert(data.msg);
                }
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }
    function deleteCustomExtension(){
        $('.loading-page').show();

        let code = $(event.target.previousElementSibling).text()
        let url = "/flow/extensions/" + g_funcKey + "," + code;
        let targetElement = $(event.target.parentElement);

        $.ajax({
            url:url,
            type : 'DELETE',
            dataType:'json',
            contentType: 'application/json',
            success : function(data){
                if(data.result) {
                    targetElement.remove();
                    displayCount();
                } else {
                    alert(data.msg);
                }
            },
            error : function(data){
                alert("Communication error with server.");
            },
            complete : function(){
                $('.loading-page').hide();
            }
        });
    }

    function displayCount(){
        $('#totalCnt').text($('.ex-item').length + " / 200");
    }
</script>

</html>
