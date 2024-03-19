import pandas as pd

from numpy import ndarray

"""
 Внимание!
 В проверяющей системе имеется проблема с catboost.
 При использовании этой библиотеки, в скрипте с решением необходимо инициализировать метод с использованием `train_dir` как показано тут:
 CatBoostRegressor(train_dir='/tmp/catboost_info')
"""


def train_model_and_predict(train_file: str, test_file: str) -> ndarray:
    """
    This function reads dataset stored in the folder, trains predictor and returns predictions.
    :param train_file: the path to the training dataset
    :param test_file: the path to the testing dataset
    :return: predictions for the test file in the order of the file lines (ndarray of shape (n_samples,))
    """

    df_train = pd.read_json(train_file, lines=True)
    df_test = pd.read_json(test_file, lines=True)

    # remove categorical variables

    for column in ['genres', 'directors', 'filming_locations', 'keywords']:
        del df_train[column]
        del df_test[column]

    for i in range(3):
        del df_train[f"actor_{i}_gender"]
        del df_test[f"actor_{i}_gender"]

    y_train = df_train["awards"]
    del df_train["awards"]
    args = {'learning_rate': 0.12, 'max_depth': 10, 'n_estimators': 147}
    regressor = GradientBoostingRegressor(**args)
    regressor.fit(df_train.to_numpy(), y_train.to_numpy())
    return regressor.predict(df_test.to_numpy())