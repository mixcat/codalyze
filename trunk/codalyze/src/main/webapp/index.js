$(function() {
	YAHOO.widget.Chart.SWFURL = "http://yui.yahooapis.com/2.5.2/build//charts/assets/charts.swf";
	
	function getDataSource(jsonData) {
		var dataSource = new YAHOO.util.DataSource(jsonData);
		dataSource.responseSchema = {fields: []};
		
		var i = 0;
		for (var name in jsonData[0]) {
			dataSource.responseSchema.fields[i++] = name;
		}

		dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
		return dataSource;
	}
	
	function renderDataTable(targetContainer, jsonData) {
		var dataSource = getDataSource(jsonData);
		var columnDefs = [];
		var i = 0;
		for (var name in jsonData[0]) {
			columnDefs[i++] = {key:name, label:name};
		}
		var myDataTable = new YAHOO.widget.DataTable(targetContainer, columnDefs, dataSource); 
	}
	
	function renderDataChart(targetContainer, jsonData) {
		var dataSource = getDataSource(jsonData);
		var fields = { xField: "id", yField: "sum" };
		var myChart = new YAHOO.widget.ColumnChart( targetContainer, dataSource,fields);
	}
	
	function manageBoundErrors(errors, bindInfo) {
		for (var key in bindInfo) {
			if (errors[key]) {
				$(bindInfo[key]).removeClass("hidden");
				$(bindInfo[key]).toggleClass("error");
				$(bindInfo[key]).after("<label class=\'errorInfo\'>"+errors[key]+"</label>")
			}
		}
	}
	
	function resetErrors(containerSelector) {
		$(containerSelector + " .errorInfo").remove();
		$(containerSelector + " *").removeClass("error");
	}
	
	var basePath = "/codalyze/api/v1/query/sql/";
	function loadSavedQuery(title) {
		$.ajax({
			type:"GET",
			url: basePath + encodeURI(title),
			success: function(responseBody) {
				var sqlQuery = eval("sqlQuery=" + responseBody);
				$('#sql-query-title-edit').val(sqlQuery.title);
				$('#sql-query-title-display').html(sqlQuery.title);
				
				$('#sql-query-edit').val(sqlQuery.query);
				$('#sql-query-display').html(sqlQuery.query);
				executeSqlQuery();
				switchToMode('mode-display');
			}
		});
	}
	
	function loadSavedQueries() {
		$.ajax({
			type:"GET",
			url: basePath,
			dataType: "script",
			success: function(responseBody) {
				var result = eval(responseBody);
				for (var sqlQuery in result) {
					$('#saved-queries').html("<li>" + result[sqlQuery].title + "</li>");
				}
				$("#saved-queries li").click(function() {
					loadSavedQuery($(this).html());
				});
			}
		});
	}
	
	function displayQueryResult(data) {
		renderDataChart("sql-query-result-chart", data);
		$('#sql-query-result-raw').html(data);
		renderDataTable("sql-query-result-table", eval(data));
	}
	
	function executeSqlQuery() {
		var bindInfo = { query:'#sql-query-edit'}
		$.ajax({
			type: "GET",
			url: basePath + encodeURI($(bindInfo.query).val().replace('\n', ' ') + "/execute"),
			dataType: "script",
			beforeSend: function (xhr) {
				resetErrors('#sql-query');
			},
			success: function(data){
				displayQueryResult(data)
			},
			error: function(xhr, message, object) {
				alert(xhr.responseText);
				manageBoundErrors(eval(xhr.responseText), bindInfo);
			}
		});
	}
	
	function createSqlQuery() {
		var bindInfo = { title:'#sql-query-title-edit', query:'#sql-query-edit'}
		$.ajax({
			type: "POST",
			url: basePath,
			data: { title:$(bindInfo.title).val(), query:$(bindInfo.query).val().replace('\n', ' ')},
			dataType: "json",	
			beforeSend: function (xhr) {resetErrors('#sql-query');},
			success: function(responseBody){
				switchToMode('mode-edit');
				loadSavedQueries();
				executeSqlQuery();
			},
			error: function (xhr, message, object) {
				manageBoundErrors(eval(xhr.responseText), bindInfo);
			}
		});
	}
	
	loadSavedQueries();
	$("#execute-sql-query").click(executeSqlQuery);
	$("#create-sql-query").click(function() {
		createSqlQuery();	
	});
	
	switchToMode(getCurrentMode());
	$('#sql-query-enable-edit').click(function() { switchToMode('mode-edit')});
	$('#sql-query-create-new').click(function() { switchToMode('mode-create')});
	$('#sql-query-save-as').click(function() { switchToMode('mode-create')});

	$('#sql-query-abort-create').click(function() {
		$('#sql-query-title-edit').val("");
		
	});
	
	/** MODE MANAGER **/
	function getCurrentMode() {
		return $("#sql-query-mode").val();
	}
	
	function switchToMode(mode) {
		var modes = {};
		modes['mode-transient'] = setModeTransient;
		modes['mode-create'] = setModeCreate;
		modes['mode-edit'] = setModeEdit;
		modes['mode-display'] = setModeDisplay;
		modes[mode]();
	}
	
	function setModeTransient() {
		$('.mode-edit, .mode-create, .mode-display').addClass('hidden');
		$('.mode-transient').removeClass('hidden');
		$("#sql-query-mode").val("mode-transient");
	}
	
	function setModeCreate() {
		$('.mode-edit, .mode-transient, .mode-display').addClass('hidden');
		$('.mode-create').removeClass('hidden');
		$("#sql-query-mode").val("mode-create");
	}
	
	function setModeEdit() {
		$('.mode-create, .mode-transient, .mode-display').addClass('hidden');
		$('.mode-edit').removeClass('hidden');
		$("#sql-query-mode").val("mode-edit");
	}
	
	function setModeDisplay() {
		$('.mode-edit, .mode-transient, .mode-create ').addClass('hidden');
		$('.mode-display').removeClass('hidden');
		$("#sql-query-mode").val("mode-display");
	}
	
});