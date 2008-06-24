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
		var myChart = new YAHOO.widget.ColumnChart( targetContainer, dataSource, fields);
	}
	
	function renderdoubleDataChart(targetContainer, jsonData, seriesDef) {
		var dataSource = getDataSource(jsonData);

		var fieldsDef = {
				xField: 'id',
				series: seriesDef
		};
		var myChart = new YAHOO.widget.ColumnChart( targetContainer, dataSource, fieldsDef );
	}
	
	function manageBoundErrors(errors, bindinfo) {
		if (!errors || !bindinfo) {
			return;
		}
		
		for (var idx in errors) {
			var error = errors[idx];
			if (bindinfo[error.field]) {
				$(bindinfo[error.field]).removeClass("hidden");
				$(bindinfo[error.field]).toggleClass("error");
				$(bindinfo[error.field]).after("<label class=\'errorInfo\'>"+error.code+"</label>");
			}
		}
	}
	
	function manageClientError(errorObject) {
		$('#application-error').append('<span>'+errorObject.message+'</span>');
	}
	
	function resetErrors(containerSelector) {
		$(containerSelector + " .errorInfo").remove();
		$(containerSelector + " *").removeClass("error");
	}
	
	function displaySqlQuery(sqlQuery) {
		$('#sql-query-id').val(sqlQuery.id);
		$('#sql-query-title-edit').val(sqlQuery.title);
		$('#sql-query-title-display').html(sqlQuery.title);
		
		$('#sql-query-edit').val(sqlQuery.query);
		$('#sql-query-display').html(sqlQuery.query);
	}
	
	var basePath = "/codalyze/api/v1/query/sql/";
	function loadSavedQuery(title) {
		$.ajax({
			type:"GET",
			url: basePath + encodeURI(title),
			success: function(responseBody) {
				var sqlQuery = eval("sqlQuery=" + responseBody);
				displaySqlquery(sqlQuery);
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
				$('#saved-queries').empty();
				for (var sqlQuery in result) {
					$('#saved-queries').append("<li>" + result[sqlQuery].title + "</li>");
				}
				$("#saved-queries li").click(function() {
					loadSavedQuery($(this).html());
				});
			}
		});
	}
	
	function displayQueryResult(data) {
		var columns = ['main_ncss','test_ncss','ncss'];
		var seriesDef = [];
		for (var idx in columns) {
			seriesDef[idx] = {displayName:columns[idx], yField:columns[idx]};
		}
		renderdoubleDataChart("sql-query-result-chart", eval(data), seriesDef);
		renderDataTable("sql-query-result-table", eval(data));
		//$('#sql-query-result-raw').html(data);
	}
	
	function executeSqlQuery() {
		var bindInfo = { query:'#sql-query-edit'}
		$.ajax({
			type: "POST",
			url: "/codalyze/api/v1/query/execute/sql/",
			data: { query:$(bindInfo.query).val().replace('\n', ' ') },
			dataType: "script",
			beforeSend: function (xhr) {
				resetErrors('#sql-query');
			},
			success: function(data){
				displayQueryResult(eval(data));
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
			success: function(sqlQuery){
				displaySqlQuery(sqlQuery);
				switchToMode('mode-edit');
				loadSavedQueries();
				executeSqlQuery();
			},
			error: function (xhr, message, object) {
				if (xhr.status == 200 && message=='error') {
					manageClientError(object);
				}
				manageBoundErrors(eval(xhr.responseText), bindInfo);
			}
		});
	}
	
	function updateSqlQuery() {
		var bindInfo = { id:'#sql-query-id', title:'#sql-query-title-edit', query:'#sql-query-edit'}
		$.ajax({
			type: "PUT",
			url: basePath + $(bindInfo.title).val(),
			data: { id:$(bindInfo.id).val(), title:$(bindInfo.title).val(), query:$(bindInfo.query).val().replace('\n', ' ')},
			dataType: "json",	
			beforeSend: function (xhr) {resetErrors('#sql-query');},
			success: function(responseBody) {
				switchToMode('mode-display');
				loadSavedQueries();
				executeSqlQuery();
			},
			error: function (xhr, message, object) {
				manageBoundErrors(eval(xhr.responseText), bindInfo);
			}
		});
	}
	
	loadSavedQueries();
	$("#execute-sql-query").click(function() {
		executeSqlQuery();
	});
	$("#create-sql-query").click(function() {
		createSqlQuery();	
	});
	$("#update-sql-query").click(function() {
		updateSqlQuery();
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