package com.caseyjbrooks.clog.log;

public interface IClog extends
        ICommonClog,
        IVerboseClog,
        IDebugClog,
        IInfoClog,
        IDefaultClog,
        IWarningClog,
        IErrorClog,
        IFatalClog {
}
