<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="#">
    <title>FLOW - 파일 확장자 차단</title>

    <link rel="stylesheet" href="/resources/css/common.css">
    <script type="text/javascript" src="/resources/jQuery/jquery-3.6.0.min.js"></script>

</head>

<body>
<section>
    <h1> ⛔ 파일 확장자 차단 </h1>
    <hr>
    <table class="extension-tbl">
        <tbody>
        <tr class="fixed-ex-row">
            <th>고정 확장자</th>
            <td>
                <span><input type="checkbox" name="bat" id="fixedEx01"><label for="fixedEx01">bat</label></span>
                <span><input type="checkbox" name="cmd" id="fixedEx02"><label for="fixedEx02">cmd</label></span>
                <span><input type="checkbox" name="com" id="fixedEx03"><label for="fixedEx03">com</label></span>
                <span><input type="checkbox" name="cpl" id="fixedEx04"><label for="fixedEx04">cpl</label></span>
                <span><input type="checkbox" name="exe" id="fixedEx05"><label for="fixedEx05">exe</label></span>
                <span><input type="checkbox" name="scr" id="fixedEx06"><label for="fixedEx06">scr</label></span>
                <span><input type="checkbox" name="js" id="fixedEx07"><label for="fixedEx06">js</label></span>
            </td>
        </tr>
        <tr class="custom-ex-row">
            <th>커스텀 확장자</th>
            <td>
                <div><input class="box" type="text" id="customExTxt" placeholder="확장자 입력"><input class="box" type="button" value="+추가" onclick="addCustomExtension()"></div>
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
    $(document).ready(function() {

        // 고정 확장자 체크

    });

    function addCustomExtension(){
        let tempExtension = $('#customExTxt').val();

        // 제한사항 체크
        if($('.ex-item').length >= 200){
            return alert("커스텀 확장자는 최대 200개 까지 추가할 수 있습니다.");
        }
        if(tempExtension.length > 20 || tempExtension.length == 0){
            return alert("커스텀 확장자의 길이는 최소 1자, 최대 20자 입니다.");
        }

        /*

            server에 추가 완료 시 화면에 추가

        */

        // customExList에 추가
        let innerHtml = "<div class=\"ex-item\"><span>" + tempExtension + "</span><button onclick=\"deleteCustomExtension()\">×</button></div>";
        $('#customExList').append(innerHtml);

        // 입력 폼 초기화
        $('#customExTxt').val("");

        displayCount();
    }

    function deleteCustomExtension(){
        /*

            server에 삭제 완료 시 화면에서 삭제

        */

        $(event.target.parentElement).remove();

        displayCount();
    }

    function displayCount(){
        $('#totalCnt').text($('.ex-item').length + " / 200");
    }
</script>

</html>
